package ru.ozon;


import io.qameta.allure.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.fail;
import static ru.ozon.Source.*;

/** Задание 1 **/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecondTest {
    static Properties props = new Properties();
    private final int minPrice = 10000;
    private final int maxPrice = 11000;

    /* Открытие главной страницы */
    @Test()
    @Description("Go to the main page of Ozon.ru")
    public void Test1_MainPage() {
        RunChromeDriver();
        try {
            props.load(new FileInputStream("config.ini"));
            String drv = props.getProperty("webdriverName");
            String drv2 = props.getProperty("webdriverPath");
            System.setProperty(props.getProperty("webdriverName"),
                    props.getProperty("webdriverPath"));
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get(props.getProperty("urlSite"));
            timeOut();
            driver.getKeyboard().pressKey(Keys.ESCAPE); // Закрытие модального окна
            timeOut();
            createScreenshot("OzonMain");
        }
        catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Открытие каталога */
    @Test()
    @Description("Go to Catalog.")
    public void Test2_Catalog() {
        try {
            clickXpath(props.getProperty("xpCatalog"));
            timeOut();
            createScreenshot("OzonCatalog");
        }
        catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Раздел Бытовая техника */
    @Test()
    @Description("Go to Appliances.")
    public void Test3_Appliances() {
        try {
            clickXpath(props.getProperty("xpAppliances"));
            timeOut();
            createScreenshot("OzonAppliances");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Раздел Техника для кухни */
    @Test()
    @Description("Go to Kitchen Appliances.")
    public void Test4_KitchenAppliances() {
        try {
            clickXpath(props.getProperty("xpKitchApl"));
            timeOut();
            createScreenshot("OzonKitchenAppliances");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }

    }

    /* Раздел Кофеварки и кофемашины */
    @Test()
    @Description("Go to Coffee Makers and Coffee Machiens.")
    public void Test5_CoffeeMaker() {
        try {
            clickXpath(props.getProperty("xpCoffeeMaker"));
            timeOut();
            createScreenshot("OzonCoffeeMaker");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Выбор типа кофеварки - рожковая */
    @Test()
    @Description("Select carob coffee maker.")
    public void Test6_CarobCoffeeMaker() {
        try {
            clickXpath(props.getProperty("xpCarobCoffee"));
            timeOut();
            createScreenshot("CarobCoffeeMaker");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Выбор метода приготовления - подогрев чашек */
    @Test()
    @Description("Select heated cups.")
    public void Test7_HeatedCups() {
        try {
            clickXpath(props.getProperty("xpHeatedCups"));
            timeOut();
            driver.getKeyboard().pressKey(Keys.PAGE_UP);
            createScreenshot("HeatedCups");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Определение диапазона цен */
    @Test()
    @Description("Select Price Range from 10000 to 11000 rubles. " +
            "Check for the prices of goods are correct.")
    public void Test8_PriceRange() {
        try {
            String minStr = Integer.toString(minPrice);
            String maxStr = Integer.toString(maxPrice);
            doubleClickXpath(props.getProperty("xpRangeLeft"));
            driver.getKeyboard().sendKeys(minStr);
            driver.getKeyboard().pressKey(Keys.ENTER);
            timeOut();
            doubleClickXpath(props.getProperty("xpRangeRight"));
            driver.getKeyboard().sendKeys(maxStr);
            driver.getKeyboard().pressKey(Keys.ENTER);
            timeOut();
            createScreenshot("OzonPriceRange");
            timeOut();
            WebElement div = findElementByClassWait(20, "b7t2");
            List<WebElement> tegAs = div.findElements(By.tagName("a"));
            int max = 1;
            for (WebElement a : tegAs) {
                if (isNumeric(a.getText())) {
                    int tmp = Integer.parseInt(a.getText());
                    if (tmp > max) max = tmp;
                }
            }
            timeOut();
            checkEveryProductOnPage();
            for (int i = 2; i <= max; i++) {
                clickXpath(props.getProperty("xpInputPage"));
                Source.driver.getKeyboard().sendKeys(Integer.toString(i));
                driver.getKeyboard().pressKey(Keys.ENTER);
                timeOut();
                checkEveryProductOnPage();
                if (i == max) {
                    div = findElementByClassWait(20, "b7t2");
                    tegAs = div.findElements(By.tagName("a"));
                    for (WebElement a : tegAs)
                        if (isNumeric(a.getText())) {
                            int tmp = Integer.parseInt(a.getText());
                            if (tmp > max) max = tmp;
                        }
                }
            }
            timeOut();
            if (max > 1) {
                driver.navigate().back();
                clickXpath(props.getProperty("xpInputPage"));
                driver.getKeyboard().sendKeys("1");
                driver.getKeyboard().pressKey(Keys.ENTER);
                timeOut();
            }
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }

    }

    /* Сортировка цены по возрастанию */
    @Test()
    @Description("Select sort in ascending order of price.")
    public void Test9_SortAsc() {
        try {
            // Открытие выпадающего списка
            clickXpath(props.getProperty("xpDropDownList"));
            timeOut();
            driver.getKeyboard().sendKeys(Keys.DOWN, Keys.DOWN);
            timeOut();
            driver.getKeyboard().pressKey(Keys.ENTER);
            timeOut();
            createScreenshot("OzonSortAsc");
            timeOutMax();
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Добавление в корзину */
    @Test()
    @Description("Add first good to the Basket.")
    public void Test_10_AddToBasket() {
        try {
            clickXpath(props.getProperty("xpAddBasket"));
            timeOut();
            createScreenshot("OzonAddBasket");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        }
    }

    /* Переход в корзину */
    @Test()
    @Description("Go to the Basket. Increase the amount of goods to 3. " +
            "Check that increase price is correct.")
    public void Test_11_Basket()  {
        try {
            clickXpath(props.getProperty("xpBasket"));
            timeOut();
            createScreenshot("OzonMyBasket");
            // Получение цены из корзины
            String price1 = findElementByXpathWait(20,
                    props.getProperty("xpPriceBasket")).getText();
            // Увеличиваем кол-во до 3-ех
            clickXpath(props.getProperty("xpDropDownCount"));
            Source.driver.getKeyboard().sendKeys(Keys.DOWN, Keys.DOWN);
            Source.driver.getKeyboard().sendKeys(Keys.ENTER);
            timeOut();
            createScreenshot("OzonFinalScreen");
            // Сравнение цен
            String price2 = findElementByXpathWait(20,
                    props.getProperty("xpSumBasket")).getText();
            // Выдать ошибку при несоответствии цены
            if (buildInt(price1) * 3 != buildInt(price2))
                fail("Кол-во товаров меньше 3-ех, либо цена некорректна.");
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
        } finally {
            driver.quit();
        }
    }

    /* Вспомогательные функции */
    private int getMinPrice(List<WebElement> prices)
    {
        List<String> pricesStr = new ArrayList<>();
        for (WebElement p : prices)
            pricesStr.add(p.getText());
        if (prices.size() > 1)
            return Math.min(buildInt(pricesStr.get(0)),
                    buildInt(pricesStr.get(1)));
        else return buildInt(pricesStr.get(0));
    }

    public void checkEveryProductOnPage() {
        StringBuilder sb = new StringBuilder();
        List<WebElement> priceElements =
                findElementsByClassWait(20, "b5v4");
        for (WebElement element : priceElements) {
            List<WebElement> prices = element.findElements(By.tagName("span"));
            int min = getMinPrice(prices);
            if (min > maxPrice || min < minPrice) {
                sb.append("\n");
                sb.append(min);
            }
        }
        if (sb.length() > 0)
            fail("Цены продуктов не соответствует условиям выборки\n"
                    + sb.toString());
    }

}