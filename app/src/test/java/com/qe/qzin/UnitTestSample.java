package com.qe.qzin;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by Shyam Rokde on 3/10/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class UnitTestSample {
  private static final String FAKE_STRING = "HELLO WORLD";

  @Mock
  Context mMockContext;

  @Test
  public void readStringFromContext_LocalizedString(){
    // Given a mocked Context injected into the object under test...
    when(mMockContext.getString(R.string.hello_world))
        .thenReturn(FAKE_STRING);

    assertThat(mMockContext.getString(R.string.hello_world), is(FAKE_STRING));
  }
}
