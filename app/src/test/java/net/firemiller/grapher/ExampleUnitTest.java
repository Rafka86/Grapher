package net.firemiller.grapher;

import net.firemiller.grapher.controller.util.Expression;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);
  }

  @Test
  public void expression_isCorrect() throws Exception {
    Expression exp = new Expression("1 + 2");
    assertEquals(3.0f, exp.Eval(), 1e-2);
    exp.Remake("sin(PI/2)");
    assertEquals(1.0f, exp.Eval(), 1e-2);
  }
}