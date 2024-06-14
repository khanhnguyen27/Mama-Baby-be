package com.myweb.mamababy.services.Article;

import com.myweb.mamababy.components.JwtTokenUtil;
import com.myweb.mamababy.dtos.ArticleDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.*;
import com.myweb.mamababy.responses.article.ArticleResponse;
import com.myweb.mamababy.services.User.IUserService;
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
                .created_at(new Date())
                .updated_at(new Date())
                .status(articleDTO.getStatus())
                .build();
        return articleReponsitory.save(newArticle);
    }

    @Override
    public Article getArticleById(int id) throws Exception {
        return articleReponsitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @Override
    public Page<ArticleResponse> getArticlesByStoreId(String keyword, int storeId, String token, PageRequest pageRequest) throws Exception {


            User retrievedUser = userService.getUserDetailsFromToken(token);
            Store existingStore = storeRepository
                    .findByUserId(retrievedUser.getId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find store with id: "+ retrievedUser.getId()));
            if (existingStore.getId() != storeId) {
                throw new Exception("Store does not match");
            } else {
                Page<Article> articlesPage= articleReponsitory.searchArticlesByStore(keyword, storeId, pageRequest);
                return articlesPage.map(ArticleResponse::fromArticle);
            }

    }

    @Override
    public Page<ArticleResponse> getAllArticle(String keyword, int storeId, PageRequest pageRequest) throws Exception {
        Page<Article> articlesPage= articleReponsitory.searchArticles(keyword, storeId, pageRequest);
        return articlesPage.map(ArticleResponse::fromArticle);
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
                existingArticle.setUpdated_at(new Date());
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
