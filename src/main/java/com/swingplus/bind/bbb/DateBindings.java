package com.swingplus.bind.bbb;

/**
 * Wraps BetterBeansBinding to provide operations that bind the value of date related Swing components to a date
 * property on a bean. This does not include binding a date to a text component, those can be found in
 * {@link TextBindings}.
 * <p>
 * NB. the operations are currently commented out because they are for third party Swing components JCalendar and
 * JDateChooser.
 * </p>
 * 
 * @author Stephen Neal
 * @since 20/04/2011
 */
class DateBindings {

    // Dependency on JCalendar required...
    // /**
    // * Create a binding of the bean property to the {@link JCalendar} date selection. The date chooser is updated with
    // the
    // * value from the property.
    // * <p>
    // * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code
    // Binder}
    // * which ensures bindings are properly managed.
    // * </p>
    // *
    // * @param bean bean
    // * @param propertyName name of the property on the bean
    // * @param component date chooser component
    // * @return binding instance
    // */
    // public static <B, V> Binding<B, Calendar, JCalendar, Calendar> calendar(B bean, String propertyName,
    // JCalendar component) {
    // Property<B, Calendar> bP = BeanProperty.create(propertyName);
    // Property<JCalendar, Calendar> cP = SwingProperty.create("calendar");
    // return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, component, cP);
    // }
    //
    // /**
    // * Create a binding of the bean property to the {@link JDateChooser} "date" property. The date chooser is updated
    // with
    // * the value from the property.
    // * <p>
    // * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code
    // Binder}
    // * which ensures bindings are properly managed.
    // * </p>
    // *
    // * @param bean bean
    // * @param propertyName name of the property on the bean
    // * @param component date chooser component
    // * @return binding instance
    // */
    // public static <B, V> Binding<B, Date, JDateChooser, Date> date(B bean, String propertyName, JDateChooser
    // component)
    // {
    // Property<B, Date> bP = BeanProperty.create(propertyName);
    // Property<JDateChooser, Date> cP = SwingProperty.create("date");
    // return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, component, cP);
    // }

}
