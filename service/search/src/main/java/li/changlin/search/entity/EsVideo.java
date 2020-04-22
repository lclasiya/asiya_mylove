package li.changlin.search.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Document(indexName = "video" ,type = "docs",shards = 1,replicas = 0)
public class EsVideo implements Serializable {
    private static final long serialVersionUID = -6165405656190817844L;


    @Id
    private Integer videoID;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String userName;


    @Field(type = FieldType.Keyword, index = false)
    private String images;
    @Field(type = FieldType.Keyword, index = false)
    private String videoUrl;

    @Field(type = FieldType.Keyword, index = false)
    private String videoLength;

    @Field(type = FieldType.Keyword, index = false)
    private String videoQua;

    @Field(type = FieldType.Keyword)
    private String tags;

    @Field(type = FieldType.Keyword)
    private String object;

    private Timestamp createTime;

    @Field(type = FieldType.Keyword, index = false)
    private List<String> imagesList;
    @Field(type = FieldType.Keyword)
    private String nagasa;
    @Field(type = FieldType.Keyword)
    private String gashitsu;
    @Field(type = FieldType.Keyword, index = false)
    private String coverImage;

    private Integer readSize = 0; // 访问量
    private Integer commentSize = 0;
    private Integer voteSize = 0;

    public EsVideo() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVideoID() {
        return videoID;
    }

    public void setVideoID(Integer videoID) {
        this.videoID = videoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getNagasa() {
        return nagasa;
    }

    public void setNagasa(String nagasa) {
        this.nagasa = nagasa;
    }

    public String getGashitsu() {
        return gashitsu;
    }

    public void setGashitsu(String gashitsu) {
        this.gashitsu = gashitsu;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getReadSize() {
        return readSize;
    }

    public void setReadSize(Integer readSize) {
        this.readSize = readSize;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public Integer getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Integer voteSize) {
        this.voteSize = voteSize;
    }
}

