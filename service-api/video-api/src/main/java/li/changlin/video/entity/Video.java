package li.changlin.video.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;


public class Video implements Serializable {

    private static final long serialVersionUID = 6921375381249361245L;


    private Integer id;

    @NotNull(message = "タイトルが必要")
    @Size(min=2, max=50,message = "二文字以上二十文字以下で入力してください")
    private String title;

    @NotNull(message = "所有者が必要")
    private String userName;

    @NotNull(message = "写真が必要")
    @Size(max = 2550)
    private String images;

    @NotNull(message = "ビデオアドレス")
    private String videoUrl;

    @NotNull(message = "ビデオが必要")
    private String videoLength;

    @NotNull()
    private String videoQua;

    private int readSize = 0; // 访问量、阅读量

    private int commentSize = 0;

    private int voteSize = 0;

    @NotNull(message = "タグが必要")
    @Size(max = 100)
    private String tags;

    @NotNull(message = "対象が必要")
    private String object;

    @NotNull()
    private Timestamp createTime;

    private String coverImage;

    public Video() {
    }

    public Video(String title,String images,String videoUrl,String videoLength,String videoQua,String tags,String object, String coverImage) {
        this.title = title;
        this.images = images;
        this.videoUrl = videoUrl;
        this.videoLength = videoLength;
        this.videoQua = videoQua;
        this.tags = tags;
        this.object = object;
        this.coverImage = coverImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getVideoQua() {
        return videoQua;
    }

    public void setVideoQua(String videoQua) {
        this.videoQua = videoQua;
    }

    public int getReadSize() {
        return readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public int getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(int voteSize) {
        this.voteSize = voteSize;
    }
}
