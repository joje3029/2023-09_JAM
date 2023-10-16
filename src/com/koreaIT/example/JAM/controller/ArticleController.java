package com.koreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.example.JAM.Article;
import com.koreaIT.example.JAM.service.ArticleService;
import com.koreaIT.example.JAM.util.DBUtil;
import com.koreaIT.example.JAM.util.SecSql;

public class ArticleController {
	Scanner sc;
	Connection conn;
	ArticleService articleService;
	
	public ArticleController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.conn =conn;
		this.articleService=new ArticleService(conn, sc);
	}

	public void dowrite() {

		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();
		int id = articleService.dowrtie(title,body);
		
		System.out.printf("%d번 게시글이 생성되었습니다\n", id);
		
	}

	public void showlist() {
		System.out.println("==게시물 목록==");

		
		ArrayList<Article> articles = articleService.showlist();

		if (articles.size() == 0) {
			System.out.println("게시글이 없습니다");
			return;
		}
		System.out.println("번호	/	제목");
		for (Article article : articles) {
			System.out.printf("%d	/	%s\n", article.id, article.title);
		}
	}

	public void showdetail(int id) {
		
		Map<String, Object> articleMap = articleService.showdetail(id);

		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		Article article = new Article(articleMap);

		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성일 : %s\n", article.regDate);
		System.out.printf("수정일 : %s\n", article.updateDate);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);

	}

	public void domodify(int id) {
		
		int articleCount=articleService.domodify(id);
		
		if (articleCount == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		System.out.printf("== %d번 게시물 수정 ==\n", id);
		System.out.printf("새 제목 : ");
		String newTitle = sc.nextLine();
		System.out.printf("새 내용 : ");
		String newBody = sc.nextLine();
		
		articleService.domodify(newTitle,newBody,id);

		System.out.println(id + "번 글이 수정되었습니다");

	}

	public void delete(int id) {
		int affectedRow = articleService.delete(id);
		
		if (affectedRow == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
			return;
		}

		System.out.printf("== %d번 게시물 삭제==\n", id);
		System.out.println(id + "번 글이 삭제되었습니다");
	}

}
