package com.example.lb.service;


import com.example.lb.utils.CommonTestUtil;
import com.example.lb.model.ServerGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.util.retry.RetryBackoffSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RetryServiceTest {

    @InjectMocks
    RetryService retryService;

    @Mock
    IServerGroupService serverGroupService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(retryService, "serverGroupService", serverGroupService);
    }

    @Test
    public void testRetryFixed()  {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup());

        RetryBackoffSpec retry = (RetryBackoffSpec) retryService.retry("profile");
        assertNotNull(retry);
        assertEquals(2, retry.maxAttempts);
        assertEquals(1000, retry.maxBackoff.toMillis());
    }

    @Test
    public void testRetryExponential()  {
        when(serverGroupService.getServerGroup(any(String.class))).thenReturn(CommonTestUtil.getServerGroup2());
        RetryBackoffSpec retry = (RetryBackoffSpec) retryService.retry("profile");
        assertNotNull(retry);
        assertEquals(2, retry.maxAttempts);
        assertEquals(1000, retry.minBackoff.toMillis());
    }

}
