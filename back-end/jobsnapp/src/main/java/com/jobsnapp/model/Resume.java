package com.jobsnapp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "resume")
public class Resume {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name") @NonNull
    private String name;
    
    @Column(name = "type") @NonNull
    private String type;
    
    @Column(name = "bytes", length = 99999999) @NonNull
    @Lob
    private byte[] bytes;
    
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "resumeFile")
    @JsonIgnoreProperties("resumeFile")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;
    
    
}
