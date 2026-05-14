package wlsh.study.lxpfronttemplate.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ScreenControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void coursesShowsMockCourseList() throws Exception {
		mockMvc.perform(get("/courses"))
			.andExpect(status().isOk())
			.andExpect(view().name("courses/list"))
			.andExpect(model().attribute("courses", hasSize(6)));
	}

	@Test
	void courseDetailShowsSelectedCourse() throws Exception {
		mockMvc.perform(get("/courses/service-planning"))
			.andExpect(status().isOk())
			.andExpect(view().name("courses/detail"))
			.andExpect(model().attributeExists("course"));
	}

	@Test
	void enrollRedirectsToCompletePage() throws Exception {
		mockMvc.perform(post("/courses/service-planning/enroll"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/enroll/complete?courseId=service-planning"));
	}

	@Test
	void authenticatedDraftPagesRenderWithoutLoginState() throws Exception {
		mockMvc.perform(get("/profile"))
			.andExpect(status().isOk())
			.andExpect(view().name("profile/edit"))
			.andExpect(model().attributeExists("member"));

		mockMvc.perform(get("/my-courses"))
			.andExpect(status().isOk())
			.andExpect(view().name("learning/list"))
			.andExpect(model().attribute("courses", hasSize(3)));
	}

	@Test
	void myCourseDetailButtonGoesToCourseDetail() throws Exception {
		mockMvc.perform(get("/my-courses"))
			.andExpect(status().isOk())
			.andExpect(content().string(org.hamcrest.Matchers.containsString("/courses/service-planning")));
	}

	@Test
	void courseCurriculumMaterialGoesToLearningScreen() throws Exception {
		mockMvc.perform(get("/courses/service-planning"))
			.andExpect(status().isOk())
			.andExpect(content().string(org.hamcrest.Matchers.containsString("/learn/service-planning?section=0&amp;lesson=0")));
	}

	@Test
	void learningCurriculumMaterialStaysInLearningScreen() throws Exception {
		mockMvc.perform(get("/learn/service-planning").param("section", "0").param("lesson", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("learning/detail"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("/learn/service-planning?section=1&amp;lesson=0")));
	}

	@Test
	void headerMenuContainsCourseListAndLogout() throws Exception {
		mockMvc.perform(get("/courses"))
			.andExpect(status().isOk())
			.andExpect(content().string(org.hamcrest.Matchers.containsString("<summary class=\"icon-link menu-toggle\"")))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("href=\"/my-courses\"")))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("href=\"/login\"")));
	}
}
