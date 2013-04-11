package com.swingplus.bind.bbb;

/**
 * Encapsulate operations that bind the value of a calendar or date component to a date field.
 * <p>
 * NB. this is not intended to be used directly except by {@code Binder}. {@link Binder} provides the public binding API
 * and operations to manage the bindings.
 * </p>
 * 
 * @author Stephen Neal
 * @since 20/04/2011
 */
class CalendarDateBindings {

    // /**
    // * Create a binding of the bean field to the {@link JCalendar} date selection. The date chooser is updated with
    // the
    // * value from the field.
    // * <p>
    // * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code
    // Binder}
    // * which ensures bindings are properly managed.
    // * </p>
    // *
    // * @param bean bean
    // * @param fieldName name of the field on the bean
    // * @param component date chooser component
    // * @return binding instance
    // */
    // public static <B, V> Binding<B, Calendar, JCalendar, Calendar> calendar(B bean, String fieldName,
    // JCalendar component) {
    // Property<B, Calendar> bP = BeanProperty.create(fieldName);
    // Property<JCalendar, Calendar> cP = SwingProperty.create("calendar");
    // return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, component, cP);
    // }
    //
    // /**
    // * Create a binding of the bean field to the {@link JDateChooser} "date" property. The date chooser is updated
    // with
    // * the value from the field.
    // * <p>
    // * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code
    // Binder}
    // * which ensures bindings are properly managed.
    // * </p>
    // *
    // * @param bean bean
    // * @param fieldName name of the field on the bean
    // * @param component date chooser component
    // * @return binding instance
    // */
    // public static <B, V> Binding<B, Date, JDateChooser, Date> date(B bean, String fieldName, JDateChooser component)
    // {
    // Property<B, Date> bP = BeanProperty.create(fieldName);
    // Property<JDateChooser, Date> cP = SwingProperty.create("date");
    // return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, component, cP);
    // }

}
