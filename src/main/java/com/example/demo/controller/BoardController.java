package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService service;

    // 목록화면
	@GetMapping("/list")
	public String list(@RequestParam(defaultValue = "0", name = "page") int page, Model model) {
		
		Page<BoardDTO> list = service.getList(page); 
		model.addAttribute("list", list);
		
		System.out.println("전체 페이지 수: " + list.getTotalPages());
		System.out.println("전체 게시물 수: " + list.getTotalElements());
		System.out.println("현재 페이지 번호: " + (list.getNumber() + 1));
		System.out.println("페이지에 표시할 게시물 수: " + list.getNumberOfElements());
	
		return "board/list";
		
	}
	
//	// 목록화면
//	@GetMapping("/list")
//	public void list(@RequestParam(defaultValue = "0", name = "page") int page, Model model) {
//		
//		Page<BoardDTO> list = service.getList(page); 
//		model.addAttribute("list", list);
//		
//		System.out.println("전체 페이지 수: " + list.getTotalPages());
//		System.out.println("전체 게시물 수: " + list.getTotalElements());
//		System.out.println("현재 페이지 번호: " + (list.getNumber() + 1));
//		System.out.println("페이지에 표시할 게시물 수: " + list.getNumberOfElements());
//	}

    // 등록화면
    @GetMapping("/register")
    public String register() {
    	
    	return "board/register";
    	
    }

    // 등록처리
    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes, Principal principal) {
    	String id = principal.getName();
		dto.setWriter(id);
    	int no = service.register(dto);
        redirectAttributes.addFlashAttribute("msg", no);
        return "redirect:/board/list";
    }

    // 상세화면
	@GetMapping("/read")
	public String read(@RequestParam(name = "no") int no, @RequestParam(defaultValue = "0", name = "page") int page, Model model) { //페이지 번호 파라미터 추가
		BoardDTO dto = service.read(no);
		model.addAttribute("dto", dto);
		model.addAttribute("page", page);
		
		return "board/read";
				
	}

    // 수정화면
    @GetMapping("/modify")
    public String modify(@RequestParam(name = "no") int no, Model model) {
        BoardDTO dto = service.read(no);
        model.addAttribute("dto", dto);
        
        return "board/modify";
    }

    // 수정처리
    @PostMapping("/modify")
    public String modifyPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        service.modify(dto);
        redirectAttributes.addAttribute("no", dto.getNo());
        return "redirect:/board/read";
    }

    // 삭제처리
    @PostMapping("/remove")
    public String removePost(@RequestParam("no") int no) {
        service.remove(no);
        return "redirect:/board/list";
    }

}
