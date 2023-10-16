package com.koreaIT.example.JAM.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.example.JAM.Article;
import com.koreaIT.example.JAM.dao.ArticleDao;

public class ArticleService {
	Scanner sc;
	Connection conn;
	ArticleDao articleDao;
	
	public ArticleService(Connection conn, Scanner sc) {
		this.sc = sc;
		this.conn =conn;
		this.articleDao = new ArticleDao(conn);
	}

	public int dowrtie(String title, String body) {
		
		int id= articleDao.dowrite(title,body);
		return id;

	}

	public ArrayList<Article> showlist() {
		ArrayList<Article> articles = new ArrayList<Article>();
		
		List<Map<String, Object>> articleListMap =articleDao.showlist();
		
		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}
		
		return articles;
	}

	public Map<String, Object> showdetail(int id) {
		return articleDao.showdetail(id);
	}

	public int domodify(int id) {
		return articleDao.domodify(id);
		
	}

	public void domodify(String newTitle, String newBody, int id) {
		articleDao.domodify(newTitle, newBody, id);
	}

	public int delete(int id) {
		return articleDao.delete(id);
	}

	}


