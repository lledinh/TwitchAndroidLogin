package com.ledinh.twitch_login.rest;

import com.google.gson.annotations.SerializedName;
import com.ledinh.twitch_login.rest.RestResponseObjects.NestedObjects.NotificationObject;
import com.ledinh.twitch_login.rest.RestResponseObjects.NestedObjects.SelfLinkObject;
import com.ledinh.twitch_login.model.UserModel;

/**
 * Created by Lam on 18/01/2016.
 */
public class RestResponseObjects {

    public interface ModelConvertible<T> {
        T convert();
    }

    public abstract static class RestObject {

    }

    public static class TopObjects {

        public static class UserObject extends RestObject implements ModelConvertible<UserModel> {
            public String type;
            public String name;
            public String created_at;
            public String updated_at;
            @SerializedName("_links")
            public SelfLinkObject link;

            public String logo;
            public String display_name;
            public String email;
            public boolean partnered;
            public String bio;
            public NotificationObject notifications;

            @Override
            public String toString() {
                return "UserObject{" +
                        "bio='" + bio + '\'' +
                        ", type='" + type + '\'' +
                        ", name='" + name + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        ", link=" + link +
                        ", logo='" + logo + '\'' +
                        ", display_name='" + display_name + '\'' +
                        ", email='" + email + '\'' +
                        ", partnered=" + partnered +
                        ", notifications=" + notifications +
                        '}';
            }

            @Override
            public UserModel convert() {
                return new UserModel(this);
            }
        }
    }

    public static class NestedObjects {
        public static class SelfLinkObject{
            public String self;
        }
        public static class NotificationObject{
            public boolean email;
            public boolean push;
        }
    }
}
