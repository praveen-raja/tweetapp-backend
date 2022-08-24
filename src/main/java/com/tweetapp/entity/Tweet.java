package com.tweetapp.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
public class Tweet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column
	@Size(min = 2, max = 144)
	private String text;
	@Column
	@Size(max = 50)
	private String tag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email", nullable = false)
	private User user;

	@OneToMany(mappedBy = "tweet", fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE })
	private List<Comment> comments = new ArrayList<>();

	@ElementCollection // 1
	private Set<String> likedBy = new HashSet<>();

	public void addNewLike(String username) {
		likedBy.add(username);
	}

	public void addNewComment(Comment comment) {
		this.comments.add(comment);
	}
}
