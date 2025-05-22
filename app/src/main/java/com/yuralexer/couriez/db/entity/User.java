package com.yuralexer.couriez.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_type")
    public String userType;

    @ColumnInfo(name = "identifier")
    public String identifier;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "name_or_company")
    public String nameOrCompany;

    public User(String userType, String identifier, String password, String nameOrCompany) {
        this.userType = userType;
        this.identifier = identifier;
        this.password = password;
        this.nameOrCompany = nameOrCompany;
    }
}