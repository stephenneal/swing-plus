package com.swingplus.bind.bbb;

/**
 * Tests for {@link DateBindings}.
 * <p>
 * This is considered and integration test because it does not test {@link AbstractBinder} in isolation, it tests real
 * bindings (which are provided by BetterBeans Binding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 18/07/2011
 */
public class DateBindingsIntegrationTest {

    // /**
    // * Test for {@link DateBindings#date(Object, String, JDateChooser)}. Verifies binding updates correctly in both
    // * directions.
    // *
    // * @throws InvocationTargetException
    // * @throws InterruptedException
    // */
    // @Test
    // public void testDateDateChooser() throws InterruptedException, InvocationTargetException {
    // // Setup
    // final TestBean bean = TestBean.newInstance();
    // final JDateChooser dateField = new JDateChooser();
    //
    // // Bind
    // Binding<TestBean, Date, JDateChooser, Date> binding = DateBindings.date(bean, "date", dateField);
    // binding.bind();
    //
    // // Test
    // assertEquals(null, bean.getString());
    // assertEquals(null, dateField.getDate());
    //
    // // Update the bean value
    // final Calendar cal = Calendar.getInstance();
    // final Date value = cal.getTime();
    // bean.setDate(value);
    // // The following test fails periodically, perhaps there is an issue with the date chooser? For now add a sleep.
    // Thread.sleep(100);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(value, dateField.getDate());
    // }
    // });
    // // Clear the date chooser
    // dateField.setDate(null);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(null, bean.getDate());
    // }
    // });
    // // Update the date chooser with a value
    // dateField.setDate(value);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(value, bean.getDate());
    // }
    // });
    // // Clear the bean value
    // bean.setDate(null);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(null, dateField.getDate());
    // }
    // });
    // }

}
