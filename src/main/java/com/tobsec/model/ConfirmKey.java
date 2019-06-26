package com.tobsec.model;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ConfirmKey implements Serializable {
    private String id;
    private int confirm_date;
    private int confirm_seq;
}