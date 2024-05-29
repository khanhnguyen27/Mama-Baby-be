package com.myweb.mamababy.services.Exchange;

import com.myweb.mamababy.dtos.ExchangeDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.ExchangeRepository;
import com.myweb.mamababy.repositories.OrderDetailRepository;
import com.myweb.mamababy.repositories.OrderRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.responses.exchange.ExchangeResponse;
import com.myweb.mamababy.responses.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangeService implements IExchangeService{

    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ExchangeRepository exchangeRepository;
    private static final String UPLOADS_FOLDER = "uploads";

    @Override
    public Exchange createExchange(ExchangeDTO exchangeDTO, MultipartFile file) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository
                .findById(exchangeDTO.getOrderDetailId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find order detail with id: "+exchangeDTO.getOrderDetailId()));
        User existingUser = userRepository
                .findById(exchangeDTO.getUserId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: "+exchangeDTO.getUserId()));
        String fileName = storeFile(file);
        Exchange newExchange = Exchange.builder()
                .orderDetail(existingOrderDetail)
                .quantity(exchangeDTO.getQuantity())
                .description(exchangeDTO.getDescription())
                .imageUrl(fileName)
                .status("PROCESSING")
                .createDate(LocalDate.now())
                .user(existingUser)
                .build();
        return exchangeRepository.save(newExchange);

    }

    @Override
    public Exchange getExchangeById(int id) throws Exception {
        Optional<Exchange> optionalExchange = exchangeRepository.findById(id);
        if (optionalExchange.isPresent()) {
            return optionalExchange.get();
        }
        throw new DataNotFoundException("Cannot find product with id =" + id);
    }

    @Override
    public Page<ExchangeResponse> getAllExchange(String status ,PageRequest pageRequest) {
        Page<Exchange> exchangePage= exchangeRepository.searchExchange(status, pageRequest);
        return exchangePage.map(ExchangeResponse::fromExchange);
    }

    @Override
    public Exchange updateExchange(int id, ExchangeDTO exchangeDTO) throws Exception {
        Exchange existingExchange = getExchangeById(id);
        if (existingExchange != null) {

//            OrderDetail existingOrderDetail = orderDetailRepository
//                    .findById(exchangeDTO.getOrderDetailId())
//                    .orElseThrow(() ->
//                            new DataNotFoundException(
//                                    "Cannot find order detail with id: "+exchangeDTO.getOrderDetailId()));
//            User existingUser = userRepository
//                    .findById(exchangeDTO.getUserId())
//                    .orElseThrow(() ->
//                            new DataNotFoundException(
//                                    "Cannot find user with id: "+exchangeDTO.getUserId()));
//
//            existingExchange.setOrderDetail(existingOrderDetail);
//            existingExchange.setUser(existingUser);
//
//            if(exchangeDTO.getQuantity() >= 0) {
//                existingProduct.setPrice(productDTO.getPrice());
//            }
            if (exchangeDTO.getStatus() != null &&
                    !exchangeDTO.getStatus().isEmpty())
                existingExchange.setStatus(exchangeDTO.getStatus());

            return exchangeRepository.save(existingExchange);
        }

        return null;
    }

    @Override
    public void deleteExchange(int id) throws IOException {
        Optional<Exchange> optionalExchange = exchangeRepository.findById(id);
        if (optionalExchange.isPresent()) {
            Exchange exchange =optionalExchange.get();
            deleteFile(exchange.getImageUrl());
            exchangeRepository.delete(exchange);
        }
    }

    @Override
    public Boolean checkFileImage(MultipartFile file) {
        Boolean result = false;
        // Kiểm tra kích thước file và định dạng
        if(file.getSize() > 10 * 1024 * 1024 || file.getOriginalFilename() == null) { // Kích thước > 10MB
            return result;
        }
        String contentType = file.getContentType();
        if(contentType == null || !contentType.startsWith("image/")) {
            return result;
        }
        result =true;
        return result;
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!checkFileImage(file)) {
            throw new IOException("Invalid image format");
        }
        //Xu li file name
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm gio hen tai vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = "Exchange_" + UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        // Đường dẫn đến thư mục chứa file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        // Đường dẫn đầy đủ đến file cần xóa
        Path filePath = uploadDir.resolve(filename);

        // Kiểm tra xem file tồn tại hay không
        if (Files.exists(filePath)) {
            // Xóa file
            Files.delete(filePath);
        } else {
            throw new FileNotFoundException("File not found: " + filename);
        }
    }
}
