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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.vaadin.allaboutgrid.Order.Priority;
import com.vaadin.data.util.BeanItemContainer;

public class OrderUtil {
    public static BeanItemContainer<Order> createOrderContainer() {
        BeanItemContainer<Order> container = new BeanItemContainer<Order>(
                Order.class);

        Random random = new Random(4837291937l);
        for (int id = 0; id < 1000; id++) {
            Order order = generateOrder(id, random);

            container.addBean(order);
        }

        return container;
    }

    // http://online-generator.com/name-generator/product-name-generator.php
    private static String[] productNames = new String[] { "Tamplam",
            "Tres Phase", "Santindex", "Biging", "Freshflex", "Statdex" };
    // "Physozefax", "Scotkix", "Nimsoft", "Stock Ozelux", "Zoomtone",
    // "Touch-Lex", "Ding Nix", "Iceing", "Tip-Kix", "Solity", "Bam Air",
    // "Confax", "Sonstring", "Freshtrax", "Qvo-Lex", "Lait", "An-Bam",
    // "Homestrong", "Volstring", "Danron", "Latdonin", "Kaytouch",
    // "Donjob", "Voya Keycore", "Sanphase", "Solfresh", "Ron-Sing",
    // "Viastrong", "Goodity", "Vaia-La", "Med Touch", "Movecore",
    // "Rankronlax", "Phystrax", "Saltair", "Ozersoft", "Tipsing"
    // "Silzumbam", "Zaamnimzap", "Tris Ronplus", "Free Phase",
    // "Softplus", "Jay-Strong", "Medit", "Tamp-Cof", "Tonstock",
    // "Roundtough", "Free-Plus", "Nam-Lam", "Voya Redeco", "Faxla",
    // "Stimtech", "Transjayzap", "Zoomdom", "Voyala", "Zontam",
    // "Biotone", "San String", "Ecolex", "Big Sanhold", "Villatex",
    // "Tamp Tantam", "Vaiatrax", "Eco-Dex", "Bamtip", "K-ex", "Span Bam",
    // "Quad-Plus", "Kanlight", "Blue-Lax", "Zon Find", "Ozer Eco",
    // "Mathold", "Physflex", "Hold Trax", "Hotcof", "U-joyfind",
    // "Plusronlab", "Solozimin", "Saileco", "Itqvosoft", "Freetex",
    // "Tempsaohold", "Zimeco", "Sail Hotfresh", "Latech", "Zonsolotouch",
    // "Spandax", "Daltfind", "Trioflex", "Warm String", "Grave Finair",
    // "Phys-It", "Solfix", "Instock", "Vaiawarm", "Kaycof", "Spangotop",
    // "Sunzap", "Moveeco", "Sun Home", "Runsing", "Topair", "Zoning",
    // "Holdtech", "Goodsoft", "X-eco", "Stantam", "Lamsanlight",
    // "Nimsaoing", "Geofix", "Zoneron", "Xxx-kix", "Vivasing",
    // "Lexifresh", "Voyajob", "Lightzoztop", "Kinron", "Techtouch",
    // "Kayity", "Dan-Lux", "Alpha Domhome", "Newzap", "Namity",
    // "Rontone", "Rancore", "Ventohotwarm", "Tripplelux", "Quotam",
    // "Subtouch", "Holdtop", "Ope Fix" };

    // http://online-generator.com/name-generator/business-name-generator.php
    private static String[] customerNames = new String[] { "Apcane", "Nimlex",
            "Rancone", "Geolab", "Aceelectrics", "Sandrill", "Haydox",
            "Vaialax", "Blackphase", "Stan-city", "o-fax", "O-lane", "matfan",
            "Haycorporation", "Dingsuntax", "goldenfase", "kay-lane", "Duolam",
            "Howis", "tresron", "Runhex", "Tanlane", "Basecane",
            "toughelectrics", "Overtech", "Trueunafan", "Trisquote",
            "Whitetechnology", "E-hatfax", "Ganjamedia", "Carecane",
            "Lattaxon", "Stattamtom", "Tranfax", "Zimdomtech", "zunron",
            "Rankplex", "Latacode", "Sunkix", "Ontodom", "Voltanex", "Latdex",
            "U-sunace", "Labvolplus", "vilaware", "Fincare", "Zun-zone",
            "Runtamtam", "Siltouch", "Damtinlab", "Carephase", "Alphadrill",
            "Tranmedia", "Sanelectronics", "X-ron", "Biodom", "Stimdexon",
            "Siliconcom", "Pluslane", "blacktax", "Daltdrill", "Keygreen",
            "Jayace", "dondax", "Newdax", "Tonice", "salt-dom", "Plextriptax",
            "Stimhow", "Indigobam", "Alphalane", "Yearlane", "Nimdexon",
            "Singletech", "Uniplanet", "Icecantom", "Zimity", "Stimzap",
            "Quoteline", "zim-techno", "temptech", "Opetrans", "Kaystrip",
            "Siliconice", "Zathphase", "Med-tam", "Vila-ing", "Bluemedia",
            "Trancity", "Iceline", "Graveit", "Basejob", "Warezatkix",
            "Apmedtex", "Donghotlam", "Sonzamcane", "Randining", "Acetam",
            "Zoombase", "Zamtech", "Namis", "Whitedom", "Sailbiola", "Dontax",
            "Basecom", "Indigola", "quadhex", "Solotaxon", "Hotcane", "Damdox", };

    private static String pickRandom(String[] values, Random random) {
        return values[random.nextInt(values.length)];
    }

    private static Order generateOrder(int id, Random random) {
        Order order = new Order(id);

        order.setCustomer(pickRandom(customerNames, random));
        order.setCustomized(random.nextDouble() > 0.75);
        order.setOrderAmount(100 + random.nextInt(2000 - 100 / 5) * 5);
        order.setOrderTime(getRandomDate(random));
        order.setPriority(getRandomPriority(random));
        order.setProduct(pickRandom(productNames, random));
        order.setReservedAmount(Math.round(order.getOrderAmount()
                * random.nextFloat()));

        return order;
    }

    private static Priority getRandomPriority(Random random) {
        double value = random.nextDouble();
        if (value < 0.1) {
            return Priority.LOW;
        } else if (value < 0.8) {
            return Priority.NORMAL;
        } else {
            return Priority.HIGH;
        }
    }

    private static Date getRandomDate(Random random) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -random.nextInt(45));
        cal.set(Calendar.HOUR_OF_DAY, 8 + random.nextInt(8));

        for (int field : new int[] { Calendar.MINUTE, Calendar.SECOND,
                Calendar.MILLISECOND }) {
            cal.set(field, random.nextInt(cal.getMaximum(field)));
        }

        return cal.getTime();
    }

    public static int getTotalOrderAmount(
            BeanItemContainer<Order> orderContainer) {
        return sumIntProperty(orderContainer, "orderAmount");
    }

    private static int sumIntProperty(BeanItemContainer<Order> orderContainer,
            String propertyId) {
        int sum = 0;

        for (Order order : orderContainer.getItemIds()) {
            Integer value = (Integer) orderContainer.getItem(order)
                    .getItemProperty(propertyId).getValue();
            sum += value.intValue();
        }

        return sum;
    }

    public static int getTotalReservedAmounT(
            BeanItemContainer<Order> orderContainer) {
        return sumIntProperty(orderContainer, "reservedAmount");
    }
}
