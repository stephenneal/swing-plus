/**
 * This is intended to be a fully functioning, easy to use binding mechanism for Swing applications. More specifically
 * the intention is to:
 * <ul>
 * <li>provide a more complete binding API with binding operations specific to Swing components</li>
 * <li>ensure Swing best practice is followed by invoking Swing component updates in the Event Dispatching Thread (see
 * {@link com.swingplus.bind.bbb.SwingProperty})</li>
 * <li>add to the converters included in BetterBeansBinding to provide a more complete "out of the box" binding</li>
 * <li>include easy management and releasing of bindings when no longer required (see
 * {@link com.swingplus.bind.bbb.BindingService})</li>
 * </p>
 * <p>
 * Binding operations are grouped by the Swing component and type of property to be bound.
 * <ul>
 * <li>{@link com.swingplus.bind.bbb.TextBindings} is for binding Swing components with a "text" property, most commonly
 * a {@link javax.swing.text.JTextComponent} or {@link javax.swing.JLabel}.</li>
 * <li>{@link com.swingplus.bind.bbb.ListBindings} is for binding Swing components that represent a list including
 * {@link javax.swing.JComboBox}, {@link javax.swing.JList}, {@link javax.swing.JTable}.</li>
 * <li>{@link com.swingplus.bind.bbb.DateBindings} was created to binding third party Swing components that provide date
 * specific functionality, i.e. calendars, date pickers etc. It is not currently supported in this project because all
 * the operations are for third party Swing components.</li>
 * </ul>
 * </p>
 * <p>
 * To read more about BetterBeansBinding go here: {@link http://kenai.com/projects/betterbeansbinding/pages/Home).
 * BetterBeansBinding is an implementation born from JSR-295 Beans Binding, {@link http://jcp.org/en/jsr/detail?id=295}.
 * </p>
 * 
 * @author Stephen Neal
 * @since 09/04/2013
 */
package com.swingplus.bind.bbb;