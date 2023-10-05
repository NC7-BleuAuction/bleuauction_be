package bleuauction.bleuauction_be.notice.controller;

import bleuauction.bleuauction_be.notice.entity.Notice;
import bleuauction.bleuauction_be.notice.service.NoticeService;
import bleuauction.bleuauction_be.notice.web.NoticeForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  //등록
    @GetMapping("/notice/new")
  public String creatForm(Model model) {
    model.addAttribute("noticeForm", new NoticeForm());
    log.info("notice/new");
    return "/notices/new";
  }

  //등록처리
  @PostMapping("/notice/new")
  public String notice(@Valid NoticeForm form) {
    Notice notice = new Notice();
    notice.setNoticeTitle(form.getNoticeTitle());
    notice.setNoticeContent(form.getNoticeContent());
    noticeService.enroll(notice);
    log.info("notice/postnew");
    return "redirect:/notices/noticeList";
  }

  //목록조회
  @GetMapping("/notices")
  public String list(Model model) {
    List<Notice> notices = noticeService.findNotices();
    model.addAttribute("notices", notices);
    return "notices/noticeList";
  }

  //삭제
  @PostMapping("/notice/{noticeNo}/delete")
  public String deleteNotice(@PathVariable("noticeNo") Long noticeNo) {
    noticeService.deleteNotice(noticeNo);
    return "redirect:/noticeList";
  }


}
