package com.yuralexer.couriez.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "orders",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE))
public class Order {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "user_id", index = true)
    public long userId;

    @ColumnInfo(name = "order_time_type")
    public String orderTimeType;

    @ColumnInfo(name = "scheduled_date_time")
    public String scheduledDateTime;

    @ColumnInfo(name = "delivery_method")
    public String deliveryMethod;

    @ColumnInfo(name = "weight_kg")
    public double weightKg;

    @ColumnInfo(name = "address_from")
    public String addressFrom;

    @ColumnInfo(name = "address_to")
    public String addressTo;

    @ColumnInfo(name = "item_description")
    public String itemDescription;

    @ColumnInfo(name = "item_value")
    public double itemValue;

    @ColumnInfo(name = "contact_phone")
    public String contactPhone;

    @ColumnInfo(name = "total_cost")
    public double totalCost;

    @ColumnInfo(name = "dimensions")
    public String dimensions;

    public Order(long userId, String orderTimeType, String scheduledDateTime, String deliveryMethod,
                 double weightKg, String addressFrom, String addressTo, String itemDescription,
                 double itemValue, String contactPhone, double totalCost, String dimensions) {
        this.userId = userId;
        this.orderTimeType = orderTimeType;
        this.scheduledDateTime = scheduledDateTime;
        this.deliveryMethod = deliveryMethod;
        this.weightKg = weightKg;
        this.addressFrom = addressFrom;
        this.addressTo = addressTo;
        this.itemDescription = itemDescription;
        this.itemValue = itemValue;
        this.contactPhone = contactPhone;
        this.totalCost = totalCost;
        this.dimensions = dimensions;
    }
}