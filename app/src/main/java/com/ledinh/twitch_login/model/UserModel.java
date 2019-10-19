package com.ledinh.twitch_login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ledinh.twitch_login.rest.UserJSON;

/**
 * Created by Lam on 18/01/2016.
 */
public class UserModel implements Parcelable {
    public String type;
    public String name;
    public String created_at;
    public String logo;
    public String display_name;

    public UserModel(UserJSON userJSON) {
        type = userJSON.getData().get(0).getType();
        name = userJSON.getData().get(0).getLogin();
        created_at = null;
        logo = userJSON.getData().get(0).getProfileImageUrl();
        display_name = userJSON.getData().get(0).getDisplayName();
    }

    protected UserModel(Parcel in) {
        type = in.readString();
        name = in.readString();
        created_at = in.readString();
        logo = in.readString();
        display_name = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(logo);
        dest.writeString(display_name);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "created_at='" + created_at + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", display_name='" + display_name + '\'' +
                '}';
    }
}
