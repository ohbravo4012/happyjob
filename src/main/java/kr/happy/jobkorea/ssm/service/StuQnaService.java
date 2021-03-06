package kr.happy.jobkorea.ssm.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StuQnaService {
	
	List<Map<String,Object>> qnaList(Map<String, Object> paramMap);
	
	Map<String,Object> selectQna(Map<String, Object> paramMap);

	List<Map<String, Object>> selectQnaList(Map<String, Object> paramMap);

	int getTotalCnt(Map<String, Object> paramMap);

	void insertQna(Map<String, Object> paramMap);

	void updateQna(Map<String, Object> paramMap);

	List<Map<String, Object>> getListReply(Map<String, Object> paramMap);

	void insertReply(Map<String, Object> paramMap);

	void insertReply2(Map<String, Object> paramMap);

	void updateQnaFile(Map<String, Object> paramMap, HttpServletRequest request) throws Exception;

	void insertQnaFile(Map<String, Object> paramMap, HttpServletRequest request) throws Exception;

	void deleteQna(Map<String, Object> paramMap);
}
