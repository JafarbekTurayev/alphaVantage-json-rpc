package com.example.forexjsonrpc.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "request_id")
    private String requestId;
    @Column(name = "function_name")
    private String functionName;
    @Column(name = "request_details",columnDefinition = "text")
    private String requestDetails;
    @Column(name = "response_details",columnDefinition = "text")
    private String responseDetails;
    @Column(name = "created_at")
    private Timestamp createdAt;

}
