package com.freightboard.ops;

import com.freightboard.loads.LoadRepository;
import com.freightboard.loads.LoadStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

// TODO 2: a plain unit test with a Mockito mock (no Spring): easy to make the database "fail" on purpose.
class LoadBoardHealthIndicatorTest {

    final LoadRepository repository = mock(LoadRepository.class);
    final LoadBoardHealthIndicator indicator = new LoadBoardHealthIndicator(repository);

    @Test
    void upWithTheNumberOfOpenLoads() {
        given(repository.countByStatus(LoadStatus.OPEN)).willReturn(12L);
        Health health = indicator.health();
        assertEquals(Status.UP, health.getStatus());
        assertEquals(12L, health.getDetails().get("openLoads"));
    }

    @Test
    void downWhenTheDatabaseFails() {
        given(repository.countByStatus(LoadStatus.OPEN)).willThrow(new DataAccessResourceFailureException("connection refused"));
        Health health = indicator.health();
        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("org.springframework.dao.DataAccessResourceFailureException: connection refused",
                health.getDetails().get("error"));
    }
}
