package com.myweb.mamababy.services.Article;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.exceptions.ExpiredTokenException;
import com.myweb.mamababy.models.Article;
import com.myweb.mamababy.models.Comment;
import com.myweb.mamababy.models.Store;
import com.myweb.mamababy.models.User;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.services.User.IUserService;
import com.myweb.mamababy.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ArticleService implements IArticleService{

    private final ArticleReponsitory articleReponsitory;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService userService;


    private static final String UPLOADS_FOLDER = "uploads";
    @Override
    public Article createArticle(ArticleDTO articleDTO, MultipartFile file) throws Exception {
        Store existingStore = storeRepository
                .findById(articleDTO.getStore_id())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find store with id: "+ articleDTO.getStore_id()));

        String fileName = file.getName();
        if (!fileName.startsWith("Article")) {
            fileName = storeFile(file);
        }
        Article newArticle = Article.builder()
                .header(articleDTO.getHeader())
                .content(articleDTO.getContent())
                .link_product(articleDTO.getLink_product())
                .link_image(fileName)
                .store(existingStore)
                .date(new Date())
                .status(articleDTO.getStatus())
                .build();
        return articleReponsitory.save(newArticle);
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!checkFileImage(file)) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = filename;

        if (!uniqueFilename.startsWith("Article_")) {
            // Thêm gio hen tai vào trước tên file để đảm bảo tên file là duy nhất
            uniqueFilename = "Article_" + UUID.randomUUID().toString() + "_" + filename;
        }
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @Override
    public Article getArticleById(int id) throws Exception {
        return articleReponsitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @Override
    public List<Article> getArticlesByStoreId(int storeId, String token) throws Exception {


            User retrievedUser = userService.getUserDetailsFromToken(token);
            Store existingStore = storeRepository
                    .findByUserId(retrievedUser.getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+ retrievedUser.getId()));
            if (existingStore.getId() != storeId) {
                throw new Exception("Store does not match");
            } else {
                List<Article> articles = articleReponsitory.findByStoreId(existingStore.getId());
                if (articles == null || articles.isEmpty()) {
                    return Collections.emptyList(); // hoặc trả về null
                }
                return articles;
            }

    }

    @Override
    public List<Article> getAllArticle() throws Exception {
        return articleReponsitory.findAll();
    }

    @Override
    public Article updateArticle(int id, ArticleDTO articleDTO, String token, MultipartFile file) throws Exception {

            User retrievedUser = userService.getUserDetailsFromToken(token);
            Store existingStore = storeRepository
                    .findByUserId(retrievedUser.getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+ retrievedUser.getId()));
            if (existingStore.getId() != (articleDTO.getStore_id())) {
                throw new Exception("Store does not match");
            } else {
                Article existingArticle = getArticleById(id);
                existingArticle.setHeader(articleDTO.getHeader());
                existingArticle.setContent(articleDTO.getContent());
                existingArticle.setLink_product(articleDTO.getLink_product());
                existingArticle.setStatus(articleDTO.getStatus());

                if(file != null && !file.isEmpty()) {
                    deleteFile(existingArticle.getLink_image());
                    String fileName = storeFile(file);
                    existingArticle.setLink_image(fileName);
                }

                articleReponsitory.save(existingArticle);
                return existingArticle;
            }

    }

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

    @Override
    public void deleteArticle(int id) {
        articleReponsitory.deleteById(id);
    }
}
