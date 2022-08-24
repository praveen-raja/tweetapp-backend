package com.tweetapp.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	@NotNull(message = "Comment cannot be empty")
	@Size(max=144)
	private String commentText;
	private Date insertTime;
	private String commentedBy;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tweet_id")
	@JsonIgnore
	private Tweet tweet;

	public Comment(String commentText, String commentedBy) {
		this.commentText = commentText;
		this.commentedBy = commentedBy;
		this.insertTime = new Date();
	}

}