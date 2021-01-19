package ru.ozon;


import io.qameta.allure.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.fail;
import static ru.ozon.Source.*;

/** Задание 1 **/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ThirdTest {
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

    /* Определение диапазона цен */
    @Test()
    @Description("Select Price Range from 10000 to 11000 rubles. " +
            "Check for the prices of goods are correct.")
    public void Test6_PriceRange() {
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
            this.checkEveryProductOnPage();
            for (int i = 2; i <= max; i++) {
                clickXpath(props.getProperty("xpInputPage"));
                Source.driver.getKeyboard().sendKeys(Integer.toString(i));
                driver.getKeyboard().pressKey(Keys.ENTER);
                timeOut();
                this.checkEveryProductOnPage();
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


    /* Определение диапазона цен */
    @Test()
    @Description("Select sort in ascending order of price.")
    public void Test7_SortAsc() {
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

    /* Добавление товара в избранное */
    @Test()
    @Description("Add good with a minimal price to favorites. Check that a good was been added to favorites and price is correct.")
    public void Test8_Favorites() {
        try {
            timeOutMax();
            clickXpath(props.getProperty("xpAddFavorites"));
            timeOut();
            createScreenshot("AddToFavorites");
            timeOutMax();

            // Сохранение цены и имени товара
            List<String> selectionPagePrices = this.getChosenProductPricesListOnPage();
            timeOut();
            String productName = findElementByXpathWait(20,
                    props.getProperty("xpFavoritesPrice")).getText();
            timeOut();

            // Переход в раздел избранное
            clickXpath(props.getProperty("xpFavorites"));
            timeOut();
            createScreenshot("Favorites");
            timeOutMax();

            // Проверка наличия и цены товара в избранном
            String element = driver.findElementByLinkText(productName).getText();
            if (element.isEmpty()) {
                fail("Товар не добавился в избранное");
            }
            driver.quit();
        } catch (Exception ex) {
            fail("Error: " + ex.getMessage());
            driver.quit();
        }
    }


    /* Сортировка цены по возрастанию */
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

    public List<String> getChosenProductPricesListOnPage() {
        List<WebElement> priceElements = new WebDriverWait(driver, 30).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("b5v4")));
        //парсинг цены
        List<String> prices = this.getPricesList((priceElements.get(0)).getText()); //в массив могут попасть 2 цены в итемах со скидкой (цена со скидкой и цена до скидки)
        //удаляем пробел
        return this.fixSpaceChar(prices);
    }

    public List<String> getPricesList(String val) {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\d{1,2}\\ \\d{3}");
        Matcher matcher = pattern.matcher(val);
        while (matcher.find()) {
            result.add(val.substring(matcher.start(), matcher.end()));
        }
        return result;
    }

    public List<String> fixSpaceChar(List<String> prices) {
        List<String> result = new ArrayList<String>();
        for (String price : prices) {
            result.add(price.replaceAll("\\ ", ""));
        }
        return result;
    }

}