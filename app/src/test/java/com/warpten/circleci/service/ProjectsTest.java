package com.warpten.circleci.service;

import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.warpten.circleci.model.Project;
import com.warpten.circleci.model.Projects;
import com.warpten.circleci.modules.DataModule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import javax.inject.Inject;

import dagger.Component;
import rx.Observable;
import rx.functions.Action1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by bhaskar on 15-04-30.
 */
@SmallTest
@RunWith(JUnit4.class)
public class ProjectsTest {
    @Inject
    Projects projectsDataModel;

    @Component(modules = DataModule.class)
    public interface ProjectsTestComponent {
        void inject (ProjectsTest test);
    }

    private ProjectsTestComponent component;

    public ProjectsTest() {
        component = DaggerProjectsTest_ProjectsTestComponent.builder()
                .dataModule(new DataModule("https://circleci.com/api/v1", "8e3217869e94ac13d86bc38dfdb0b84858cb2f43"))
                .build();
        component.inject(this);
    }

    @Test
    public void testModuleSetup() {
        assertNotNull(projectsDataModel);
    }

    @Test
    public void testProjectsObservable() {
        final Observable<List<Project>> projectsObservable = projectsDataModel.getObservable();
        assertNotNull(projectsObservable);
        assertEquals(0, projectsObservable.toBlocking()
                .next()
                .iterator()
                .next()
                .size());
    }

    @Test
    public void testProjectsObservableFetch() {
        projectsDataModel.refresh();
        for (List<Project> projects : projectsDataModel.getObservable()
                .toBlocking()
                .next()) {
            assertTrue(projects.size() >= 0);
            if (projects.size() > 0) {
                return;
            }
        }
    }
}
