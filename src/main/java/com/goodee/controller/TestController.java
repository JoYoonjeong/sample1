package com.goodee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.goodee.vo.MediaVO;

@Controller
public class TestController {
	
	// Spring file upload 정책
	/* - 스프링에서는 파일을 받기 위한 스펙을 제공하고 있으며 그중 하나가 MultipartFile 클래스에 바로
	 *   바이너리 파일을 넣는 형태를 제공한다.
	 * - 형식 : @RequestParam("type="file" input 이름") MultipartFile file
	 * */
	@PostMapping("/test1")
	public String singleFileUpload(@RequestParam("mediaFile") MultipartFile file) throws IllegalStateException, IOException {
		
		// 데이터가 제대로 있다면
		if(!file.getOriginalFilename().isEmpty()) {
			// 파일의 이름에 해당하는 파일을 저장하겠다.
			Path path = Paths.get("D:/sample/"+file.getOriginalFilename());
			file.transferTo(path);
			System.out.println("매우 잘 저장되었습니다.");
		}else {
			System.out.println("에러가 발생했습니다.");
		}
		return "test1_result";
	}
	
	
	// MultipartFile을 배열처리 해서 받아주면 된다.
	@PostMapping("/test2")
	public String multiFileUpload(@RequestParam("mediaFile") MultipartFile[] files) 
			throws IllegalStateException, IOException {
		for(MultipartFile file : files) {
			if(!file.getOriginalFilename().isEmpty()) {
				// 파일의 이름에 해당하는 파일을 저장하겠다.
//				Path path = Paths.get("D:/sample/"+file.getOriginalFilename());
//				file.transferTo(path);
				file.transferTo(Paths.get("D:/sample/"+file.getOriginalFilename()));
				System.out.println(file.getOriginalFilename()+"저장완료.");
			}else {
				System.out.println("에러가 발생했습니다.");
			}
		}
		
		return "test2_result";
	}
	
	// 오버로딩 - 메서드 이름은 같지만 파라미터가 다른 경우
	@PostMapping("/test3")
	public String multiFileUpload(@RequestParam("mediaFile") MultipartFile[] files,
			@RequestParam String user, @RequestParam String url, Model model) 
			throws IllegalStateException, IOException {
		for(MultipartFile file : files) {
			if(!file.getOriginalFilename().isEmpty()) {
				file.transferTo(Paths.get("D:/sample/"+file.getOriginalFilename()));
				System.out.println(file.getOriginalFilename()+"저장완료.");
			}else {
				System.out.println("에러가 발생했습니다.");
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("url", url);
		
		return "test3_result";
	}
	
	@PostMapping("/test4")
	public String multiFileUpload(MediaVO vo) throws IllegalStateException, IOException {
		MultipartFile[] files = vo.getMediaFile();
		
		for(MultipartFile file : files) {
			if(!file.getOriginalFilename().isEmpty()) {
				file.transferTo(Paths.get("D:/sample/"+file.getOriginalFilename()));
				System.out.println(file.getOriginalFilename()+"저장완료.");
			}else {
				System.out.println("에러가 발생했습니다.");
			}
		}
		
		return "test4_result";
	}
	
	@PostMapping("/test5")
	@ResponseBody
	public String multiFileUploadWithAjax(MultipartFile[] uploadFile) 
			throws IllegalStateException, IOException {
		for(MultipartFile file : uploadFile) {
			if(!file.getOriginalFilename().isEmpty()) {
				file.transferTo(Paths.get("D:/sample/"+file.getOriginalFilename()));
				System.out.println(file.getOriginalFilename()+"저장완료.");
			}else {
				System.out.println("에러가 발생했습니다.");
			}
		}
		
		return "test5 received";
	}
	
	// 데이터 전송시에는 반드시 response 객체를 통해 전송을 해야 한다.
	@GetMapping("/download1")
	public void download(HttpServletResponse response) throws Exception{
		String path = "D:/sample/abc.txt";
		
		// 다운로드 받고자 하는 파일에 대한 Path 지정
		Path file = Paths.get(path);
		
		// 파일이름 utf-8로 인코딩 : 파일 이름이 깨지지 않도록 설정하기 위함
		String filename = URLEncoder.encode(file.getFileName().toString(), "UTF-8");
		
		// response 객체의 헤더 세팅 - 헤더가 없으면 데이터를 제대로 받아올 수 없다.
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		
		// 파일 channel 설정
		FileChannel fc = FileChannel.open(file, StandardOpenOption.READ);
		
		// response에서 output스트림 추출
		OutputStream out = response.getOutputStream();
		
		// response에서는 지원이 되는게 결국은 outputstream만 뽑아낼 수 있다.
		// response에서는 channel을 바로 뽑아낼 수 없다.
		// outputStream에서 channel 추출
		WritableByteChannel outputChannel = Channels.newChannel(out);
		
		// response 객체로 파일 전송
		fc.transferTo(0, fc.size(), outputChannel);
	}
}
