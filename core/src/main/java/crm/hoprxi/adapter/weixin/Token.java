package crm.hoprxi.adapter.weixin;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Token {
    @JsonProperty
    private String access_token;
    @JsonProperty
    private int expires_in;
    @JsonProperty
    private String errcode;
    @JsonProperty
    private String errmsg;

    public String errmsg() {
        return errmsg;
    }

    public String errcode() {
        return errcode;
    }

    public String access_token() {
        return access_token;
    }


    public int expires_in() {
        return expires_in;
    }


    @Override
    public String toString() {
        return "Token{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}

