package li.changlin.common.vo;

import java.io.Serializable;

public class Fiction implements Serializable {
    private static final Long serialVersionUID = 1L;
    private String name;
    private String url;

    public Fiction(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
