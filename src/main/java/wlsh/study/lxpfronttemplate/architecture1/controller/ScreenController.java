package wlsh.study.lxpfronttemplate.architecture1.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScreenController {

	@GetMapping("/")
	public String home() {
		return "redirect:/courses";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title", "로그인");
		return "auth/login";
	}

	@PostMapping("/login")
	public String doLogin() {
		return "redirect:/courses";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "회원가입");
		return "auth/signup";
	}

	@PostMapping("/signup")
	public String doSignup() {
		return "redirect:/courses";
	}

	@GetMapping("/courses")
	public String courses(@RequestParam(required = false, defaultValue = "") String q, Model model) {
		List<Course> filteredCourses = courses().stream()
			.filter(course -> q.isBlank() || course.title().toLowerCase(Locale.ROOT).contains(q.toLowerCase(Locale.ROOT)))
			.toList();

		model.addAttribute("title", "강의 목록");
		model.addAttribute("query", q);
		model.addAttribute("courses", filteredCourses);
		model.addAttribute("cartCount", cartCourses().size());
		return "courses/list";
	}

	@GetMapping("/courses/{courseId}")
	public String courseDetail(@PathVariable String courseId, Model model) {
		Course course = findCourse(courseId);

		model.addAttribute("title", "강의 상세");
		model.addAttribute("course", course);
		model.addAttribute("cartCount", cartCourses().size());
		return "courses/detail";
	}

	@PostMapping("/courses/{courseId}/enroll")
	public String enroll(@PathVariable String courseId) {
		return "redirect:/enroll/complete?courseId=" + courseId;
	}

	@PostMapping("/courses/{courseId}/cart")
	public String addCart(@PathVariable String courseId) {
		return "redirect:/cart";
	}

	@GetMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("title", "장바구니");
		model.addAttribute("courses", cartCourses());
		model.addAttribute("cartCount", cartCourses().size());
		return "cart/index";
	}

	@PostMapping("/cart/enroll")
	public String enrollCart() {
		return "redirect:/enroll/complete?courseId=service-planning";
	}

	@GetMapping("/enroll/complete")
	public String enrollComplete(@RequestParam(required = false, defaultValue = "service-planning") String courseId,
			Model model) {
		model.addAttribute("title", "수강 신청 완료");
		model.addAttribute("course", findCourse(courseId));
		model.addAttribute("cartCount", cartCourses().size());
		return "courses/complete";
	}

	@GetMapping("/my-courses")
	public String myCourses(Model model) {
		model.addAttribute("title", "내 강의 목록");
		model.addAttribute("courses", enrolledCourses());
		model.addAttribute("cartCount", cartCourses().size());
		return "learning/list";
	}

	@GetMapping("/learn/{courseId}")
	public String learn(@PathVariable String courseId,
			@RequestParam(required = false, defaultValue = "0") int section,
			@RequestParam(required = false, defaultValue = "0") int lesson,
			Model model) {
		Course course = findCourse(courseId);

		model.addAttribute("title", "내 강의 상세 / 수강 화면");
		model.addAttribute("course", course);
		model.addAttribute("selectedLesson", selectedLesson(course, section, lesson));
		model.addAttribute("cartCount", cartCourses().size());
		return "learning/detail";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		model.addAttribute("title", "내 정보");
		model.addAttribute("member", new Member("홍길동", "name@example.com"));
		model.addAttribute("cartCount", cartCourses().size());
		return "profile/edit";
	}

	@PostMapping("/profile")
	public String saveProfile() {
		return "redirect:/profile";
	}

	private Course findCourse(String courseId) {
		return courses().stream()
			.filter(course -> course.id().equals(courseId))
			.findFirst()
			.orElse(courses().get(0));
	}

	private List<Course> courses() {
		return List.of(
			new Course("frontend-basic", "프론트엔드 입문", "강사명", "HTML, CSS, JavaScript 기초를 다룹니다.", true,
				curriculum()),
			new Course("data-analysis", "데이터 분석 기초", "강사명", "비즈니스 데이터를 읽고 시각화하는 방법을 익힙니다.", false,
				curriculum()),
			new Course("service-planning", "서비스 기획 MVP", "강사명", "서비스 기획과 MVP 화면 설계를 학습합니다.", true,
				curriculum()),
			new Course("backend-api", "백엔드 API 설계", "강사명", "REST API와 서버 설계 흐름을 정리합니다.", false,
				curriculum()),
			new Course("ux-research", "UX 리서치 시작하기", "강사명", "사용자 인터뷰와 리서치 결과 정리를 연습합니다.", false,
				curriculum()),
			new Course("sql-basic", "SQL 기본", "강사명", "데이터 조회와 집계에 필요한 SQL 기본기를 배웁니다.", true,
				curriculum()));
	}

	private List<Course> cartCourses() {
		return courses().stream()
			.filter(Course::selected)
			.toList();
	}

	private List<Course> enrolledCourses() {
		return List.of(findCourse("service-planning"), findCourse("frontend-basic"), findCourse("sql-basic"));
	}

	private List<Section> curriculum() {
		return List.of(
			new Section("섹션 1. 강의 소개",
				List.of(new Lesson("강의자료 제목", "동영상"), new Lesson("강의자료 제목", "문서"))),
			new Section("섹션 2. 핵심 개념",
				List.of(new Lesson("강의자료 제목", "동영상"), new Lesson("강의자료 제목", "문서"))));
	}

	private Lesson selectedLesson(Course course, int sectionIndex, int lessonIndex) {
		if (sectionIndex < 0 || sectionIndex >= course.curriculum().size()) {
			return course.curriculum().get(0).lessons().get(0);
		}

		List<Lesson> lessons = course.curriculum().get(sectionIndex).lessons();
		if (lessonIndex < 0 || lessonIndex >= lessons.size()) {
			return lessons.get(0);
		}

		return lessons.get(lessonIndex);
	}

	public record Course(String id, String title, String instructor, String description, boolean selected,
			List<Section> curriculum) {
	}

	public record Section(String title, List<Lesson> lessons) {
	}

	public record Lesson(String title, String type) {
	}

	public record Member(String name, String email) {
	}
}
