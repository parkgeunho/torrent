package com.util;

public class MyUtil {	// 페이징 처리
	
	// 전체페이지 수
	// numPerPage :  한 화면에 표시할 데이터 갯수
	// dataCount : 전체 데이터 갯수
	public int getPageCount(int numPerPage, int dataCount){
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0)
			pageCount++;
		
		return pageCount;
	}
	
	// 페이징 처리 메소드
	// currentPage : 현재 표시할 페이지 번호
	// totalPage : 전체 페이지 수
	// listUrl : 링크를 설정할 url(list.jsp)
	public String pageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 5;  // ◀이전 6 7 8 9 10 다음▶
		int currentPageSetup; // 이전버튼의 숫자
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage == 0 || totalPage == 0)
			return "";
		
		// listUrl		
		// list.jsp? --> pageNum=9
		// list.jsp?searchKey=name&searchValue='suzi'& --> pageNum=9
		if(listUrl.indexOf("?") != -1)
			listUrl = listUrl + "&";
		else
			listUrl = listUrl + "?";
		
		/*//currentPageSetup
		currentPageSetup = (currentPage-1/numPerBlock)*numPerBlock;*/
		
		//currentPageSetup
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		
		// ◀이전 6 7 8 9 10 다음▶
		
		// ◀이전
		if(/*totalPage>numPerBlock &&*/ currentPageSetup>0){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" +
						currentPageSetup + "\">◀이전</a>&nbsp;");
			//<a href="list.jsp?pageNum=5">◀이전</a>&nbsp;
		}
		
		// 바로가기 페이지(6 7 8 9 10)
		page = currentPageSetup + 1;
		
		while(page<=totalPage && page<=(currentPageSetup + numPerBlock)){
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">9</font>&nbsp;
			}else{
				
				sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=6">6</a>&nbsp;
			}
			
			page++;
		}
		
		// 다음▶
		if(totalPage-currentPageSetup > numPerBlock){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" + page + "\">다음▶</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">다음▶</a>&nbsp;
		}
		
		return sb.toString();
	}
	
public String reportPageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 5;  // ◀이전 6 7 8 9 10 다음▶
		int currentPageSetup; // 이전버튼의 숫자
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage == 0 || totalPage == 0)
			return "";
		
		// listUrl		
		// list.jsp? --> pageNum=9
		// list.jsp?searchKey=name&searchValue='suzi'& --> pageNum=9
		if(listUrl.indexOf("?") != -1)
			listUrl = listUrl + "&";
		else
			listUrl = listUrl + "?";
		
		/*//currentPageSetup
		currentPageSetup = (currentPage-1/numPerBlock)*numPerBlock;*/
		
		//currentPageSetup
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		
		// ◀이전 6 7 8 9 10 다음▶
		
		// ◀이전
		if(/*totalPage>numPerBlock &&*/ currentPageSetup>0){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" +
						currentPageSetup + "\">◀이전</a>&nbsp;");
			//<a href="list.jsp?pageNum=5">◀이전</a>&nbsp;
		}
		
		// 바로가기 페이지(6 7 8 9 10)
		page = currentPageSetup + 1;
		
		while(page<=totalPage && page<=(currentPageSetup + numPerBlock)){
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">9</font>&nbsp;
			}else{
				
				sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" + page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=6">6</a>&nbsp;
			}
			
			page++;
		}
		
		// 다음▶
		if(totalPage-currentPageSetup > numPerBlock){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" + page + "\">다음▶</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">다음▶</a>&nbsp;
		}
		
		return sb.toString();
	}
	
	
	
}


















