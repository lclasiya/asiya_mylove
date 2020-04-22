package li.changlin.search.vo;

public class Comment {
    private String content;
    private String username;
    private String usericon;
    private String createTime;

    public Comment() {
    }

    public Comment(String content, String username, String usericon, String createTime) {
        this.content = content;
        this.username = username;
        this.usericon = usericon;
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }
}
