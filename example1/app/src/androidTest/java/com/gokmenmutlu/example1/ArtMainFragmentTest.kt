package com.gokmenmutlu.example1

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gokmenmutlu.artbookkotlin.R
import example1.app.activities.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/*
@RunWith(AndroidJUnit4::class)
class ArtMainFragmentTest {


    private val idlingResource = CountingIdlingResource("Network")

    @Before
    fun setUp() {
        // Global idling resource'ü kaydediyoruz
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        // Test sonrasında idling resource'ü kaldırıyoruz
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun testRecyclerViewUpdatesWithSearch() {
        // MainActivity'yi başlat
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Menüdeki "ArtBook" butonuna tıklama
        onView(withId(R.id.buttonArt)) // Bu, "ArtBook" butonunun ID'si olmalı
            .perform(click()) // Butona tıklama

        // ArtMainFragment'e geçiş yapılmış olmalı, fragment ile ilgili doğrulamayı yap
        onView(withId(R.id.recyclerView)) // RecyclerView kontrolü
            .check(matches(isDisplayed())) // RecyclerView'ın doğru şekilde göründüğünü doğrula

        // Arama yapmak için menüdeki arama ikonuna tıklayıp, metin yazma
        onView(withId(R.id.itemSearch))
            .perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(typeText("Mona Lisa")) // SearchView'a arama metnini yazma

        // RecyclerView'de ilgili öğeyi kontrol et
        onView(withId(R.id.recyclerView))
            .check(matches(hasDescendant(withText("Mona Lisa")))) // Arama sonuçları içinde öğeyi kontrol et
    }






}

 */