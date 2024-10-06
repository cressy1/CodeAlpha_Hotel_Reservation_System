package com.cressy.MWHotelReservations.services.ServiceImplementations;

import com.cressy.MWHotelReservations.entity.Rooms;
import com.cressy.MWHotelReservations.exceptions.InternalServerException;
import com.cressy.MWHotelReservations.exceptions.ResourceNotFoundException;
import com.cressy.MWHotelReservations.repositories.RoomRepository;
import com.cressy.MWHotelReservations.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    @Override
    public Rooms addNewRoom(MultipartFile file, String roomType,
                            BigDecimal roomPrice) throws SQLException, IOException {
        Rooms room = new Rooms();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();

    }

    @Override
    public List<Rooms> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Rooms> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, room not found");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return new byte[0];
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Rooms> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Rooms updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Rooms room = roomRepository.findById(roomId).
                orElseThrow(()-> new ResourceNotFoundException("Room not found"));
        if (roomType != null) {
            room.setRoomType(roomType);
            if (roomPrice != null) {
                room.setRoomPrice(roomPrice);
                if (photoBytes != null && photoBytes.length > 0) {
                    try {
                        room.setPhoto(new SerialBlob(photoBytes));
                    } catch (SQLException e) {
                        throw new InternalServerException("Error updating room");
                    }
                }
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Rooms> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    public List<Rooms> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}
