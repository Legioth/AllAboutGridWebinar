/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.allaboutgrid;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import org.vaadin.allaboutgrid.Order.Priority;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.Grid.CellStyleGenerator;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.HeaderRow;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;

@SuppressWarnings("all")
@Theme("valo")
public class AllAboutGridUI extends UI {
    @WebServlet(urlPatterns = "/*", name = "AllAboutGridServlet")
    @VaadinServletConfiguration(ui = AllAboutGridUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private final DecimalFormat idFormat = new DecimalFormat("'#'000");

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Grid grid = new Grid();

        initializeGrid(grid);
        grid.setWidth("700px");
        grid.setHeight("500px");

        applyDemoHacks(grid);

        layout.addComponent(grid);
    }

    private void initializeGrid(final Grid grid) {

        /*
         * Let's just add some data there to get something showing
         */

        // grid.addColumn("Col 1");
        // grid.addColumn("Col 2");
        //
        // grid.addRow("Some", "data");
        // grid.addRow("Another", "row");

        /*
         * Let's use a full-featured container instead
         */

        BeanItemContainer<Order> orderContainer = createOrderContainer();
        grid.setContainerDataSource(orderContainer);

        /*
         * Changing the column order and adjusting column headers
         */

        grid.setColumnOrder("id", "customer", "product", "orderAmount",
                "reservedAmount", "completePercentage", "priority",
                "customized", "orderTime");

        grid.getColumn("orderAmount").setHeaderCaption("Ordered");
        grid.getColumn("reservedAmount").setHeaderCaption("Reserved");
        grid.getColumn("completePercentage").setHeaderCaption("Complete");
        grid.getColumn("customized").setHeaderCaption("Custom");

        /*
         * Removing unwanted columns
         */

        // grid.removeColumn("customer");
        // grid.removeColumn("customized");
        // grid.removeColumn("priority");
        // grid.removeColumn("orderTime");

        /*
         * Adjusting column sizes
         */

        grid.getColumn("id").setMaximumWidth(70);

        grid.getColumn("customer").setMinimumWidth(200);
        grid.getColumn("product").setMinimumWidth(200);

        /*
         * Keep some columns in view all the time
         */

        grid.getColumn("product").setLastFrozenColumn();

        /*
         * Changing the locale affects how data is presented
         */

        grid.setLocale(Locale.GERMANY);

        /*
         * Various ways of tweaking how data is shown
         */

        grid.getColumn("id").setRenderer(new NumberRenderer(idFormat));

        grid.getColumn("completePercentage").setRenderer(
                new NumberRenderer(NumberFormat.getPercentInstance(grid
                        .getLocale())));

        grid.getColumn("completePercentage").setRenderer(
                new ProgressBarRenderer());

        grid.getColumn("customized").setConverter(
                new BooleanToFontIconConverter());

        grid.getColumn("customized").setRenderer(new HtmlRenderer());

        grid.setCellStyleGenerator(new CellStyleGenerator() {
            @Override
            public String getStyle(CellReference cellReference) {
                if ("priority".equals(cellReference.getPropertyId())) {
                    Priority priority = (Priority) cellReference.getValue();
                    return "priority-" + priority.name().toLowerCase();
                } else {
                    return null;
                }
            }
        });

        /*
         * Additional header spanned cells
         */

        HeaderRow extraHeader = grid.prependHeaderRow();

        extraHeader.join("orderAmount", "reservedAmount").setText("Quantity");

        extraHeader.join("priority", "customized").setText("Status");

        /*
         * Footer with various types of content
         */

        FooterRow extraFooter = grid.appendFooterRow();

        int totalOrdered = OrderUtil.getTotalOrderAmount(orderContainer);
        extraFooter.getCell("orderAmount").setText(
                Integer.toString(totalOrdered));

        int totalReserved = OrderUtil.getTotalReservedAmounT(orderContainer);
        extraFooter.getCell("reservedAmount").setHtml(
                "<b>" + totalReserved + "</b>");

        extraFooter.getCell("completePercentage").setComponent(
                new ProgressBar(totalReserved / (float) totalOrdered));

        /*
         * Enable editing
         */

        grid.setEditorEnabled(true);
        grid.setFrozenColumnCount(0);

        grid.getColumn("id").setEditable(false);
        grid.getColumn("completePercentage").setEditable(false);

        grid.getColumn("customized").getEditorField().setCaption("");

        grid.getColumn("orderTime").setEditorField(createOrderTimeField());

        Field<?> customerField = grid.getColumn("customer").getEditorField();
        customerField.setRequired(true);
        customerField.setRequiredError("Value is required");

        /*
         * Get an event when the users saves in the editor
         */

        grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
            @Override
            public void preCommit(CommitEvent commitEvent)
                    throws CommitException {
                // Do nothing
            }

            @Override
            public void postCommit(CommitEvent commitEvent)
                    throws CommitException {
                Notification.show("Changes saved");
            }
        });

        /*
         * New feature going into Vaadin 7.5: Column reordering
         */

        grid.setColumnReorderingAllowed(true);

        /*
         * New feature going into Vaadin 7.5: Row details
         */

        grid.setDetailsGenerator(new DetailsGenerator() {
            @Override
            public Component getDetails(RowReference rowReference) {
                Order order = (Order) rowReference.getItemId();
                String detailsMessage = "This is a label with information about the order of "
                        + order.getProduct()
                        + " by "
                        + order.getCustomer()
                        + ".";

                Button deleteButton = new Button("Delete order",
                        new Button.ClickListener() {
                            @Override
                            public void buttonClick(ClickEvent event) {
                                Notification.show("Button clicked");
                            }
                        });

                VerticalLayout layout = new VerticalLayout(new Label(
                        detailsMessage), deleteButton);
                layout.setMargin(true);
                layout.setSpacing(true);

                return layout;
            }
        });

        grid.addItemClickListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    Object itemId = event.getItemId();
                    grid.setDetailsVisible(itemId,
                            !grid.isDetailsVisible(itemId));
                }
            }
        });

        grid.setEditorEnabled(false);

        /*
         * That's all. Thank you for watching!
         */
    }

    @SuppressWarnings("unused")
    private static PopupDateField createOrderTimeField() {
        PopupDateField orderTimeField = new PopupDateField();
        orderTimeField.setResolution(Resolution.SECOND);
        return orderTimeField;
    }

    private void applyDemoHacks(Grid grid) {
        // Inject some styles so we don't need to define a custom theme
        injectStyles();

        // Work around some strangeness with the automatic width of the last col
        hackDateColumnWidth(grid);
    }

    private void hackDateColumnWidth(Grid grid) {
        List<Column> columns = grid.getColumns();
        if (columns.isEmpty()) {
            return;
        }

        Column lastCol = columns.get(columns.size() - 1);
        if (!"orderTime".equals(lastCol.getPropertyId())) {
            return;
        }

        lastCol.setWidth(174);
    }

    private void injectStyles() {
        getPage()
                .getStyles()
                .add(".priority-high {color: red; font-weight: bold;} .priority-normal {font-weight: bold}");
    }

    private static BeanItemContainer<Order> createOrderContainer() {
        return OrderUtil.createOrderContainer();
    }
}
