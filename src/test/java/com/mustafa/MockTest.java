package com.mustafa;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.*;

import static org.mockito.Mockito.*;

public class MockTest {

    /**
     * class Islem{
     *     public int topla(int sayi1, int sayi2){
     *     system.out.println("Topla metodu cagrildi");
     *         return sayi1 + sayi2;
     *   }
     * }
     *
     * mock(Islem.class) -> bu method Islem sınıfının bir sanal sınıfını olusturur.
     * bu sanal sınıf tüm varlıklarını  soyutlar. Yani tüm aksiyonlar soyutlanır.
     * islem.topla(5,4) -> normalde bu islem 9 sayısını döner, ancak mock lanan sınıf
     * bu islmde tepki vermez.
     */

    @Test
    void toplamMockTest(){
        Islem islem = new Islem();
        int sayi = islem.topla(5,6);
        System.out.println("toplam = " + sayi);
        System.out.println("---------------");

        Islem islemMock = mock(Islem.class); // sanal nesne
        sayi = islemMock.topla(43,23);
        System.out.println("mock toplam = " + sayi);

    }


    @Test
    void bolumock(){
        double bolum;
//        Islem islem = new Islem(); // hata verir 0/0
        Islem islem = mock(Islem.class);

        /**
         * Mocklanmış sınıflarda methodlar çalışmaz, yani gövdeleri yoktur. Ancak bir
         * methodun nasıl davranması gerektiğini söyleyebilirsiniz.
         * mesela;
         * seni birisi çağırır ise şu değeri dön diyebilirisiniz.
         */
        when(islem.bolum(34,0)).thenReturn(34.0); // işlem.bolum 34,0 değişkenleri ile çağrılıyor ise bunu dön
        bolum = islem.bolum(34,0);
        System.out.println("Bolum = " + bolum);
        System.out.println("-------------------");
        double ort = bolum+34;
        System.out.println("Ortalama = " + ort);
    }


    @Test
    void listTest() {
        List<String> liste = mock(ArrayList.class); // arraylistin içindeki metotların tamamı mocklanmış
        liste.add("Mustafa");
        liste.add("Deniz");
        liste.add("Murat");

        when(liste.get(30)).thenReturn("Tekin");
        System.out.println("ilk kisi = " + liste.get(0)); // get metotdu sanallaşmış bir metotdur
        System.out.println("ilk kisi = " + liste.get(30));

        /**
         *  liste adı verilen Arraylist in add metodu "Tekin" değeri ile cağrıldı mı? ->fail
         */

        verify(liste).add("Deniz"); // deniz ismiyle ile add yapıldı mı
        verify(liste).add("Murat"); //
    }


    @Test
    void verifyAnyMock(){
        List<String> liste = mock(ArrayList.class);
        when(liste.get(4)).thenReturn("Ahmet");
        System.out.println("4. kisi..: " + liste.get(4)); // get sanal bir metot
        System.out.println("5. kisi..: " + liste.get(5)); // eger 4 numaralı deger cağrılırsa ahmet gelir ancak 5 boş null

        /**
         * 1. Durum , direkt değer vererek cağırım beklediğimiz methodlar sadece o değer ile
         * cağrıldırında calışır. any... şeklinde cağırımı yapılan methodlar tüm çağrım değerleri ile çalışır.
         *
         * 2. Durum, when ile kurduğumuz handler işlemi çağrım yapılmadan önce tanımlanmalıdır. Aksi
         * halde yapılan cağrımlar çalışmaz.
         */

        when(liste.get(anyInt())).thenReturn("Genel Isim");
        System.out.println("4. kisi..: " + liste.get(40));
        System.out.println("5. kisi..: " + liste.get(5));

//        List<String> liste2 = new ArrayList<>();
//        System.out.println("8.kisi ..: " + liste2.get(8)); // IndexOutOfBoundsException fırlatır. 8 numaralı değer yok

    }

    @Test
    void verifyTime(){ // bir kere üç kere çağrılmalı, bir metod kaç kere çalışmalı, kaç kere çağrılmalı
        List<String> listem = mock(ArrayList.class);
        listem.add("Ahmet");

        listem.add("Demet");
        listem.add("Demet");

        listem.add("Leyla");
        listem.add("Leyla");
        listem.add("Leyla");

        /**
         * Bu kontrol bir method çağırımının yapılıp yapılmadığını kontrol eder.Burada aslında
         * çağrımın sayısı önemli değildir. Sadece yapılmış olması yeterlidir.
         * Ancak, belli bir sayıda çağrımın zorunlu olması isteniyor ise o zaman times(Cağrım sayısı)
         * şeklinde kullanmak gereklidir.
         * DİKKAT!!! burada girilen sayısal değerler kesin değerlerdir. Bu nedenle mutlaka o sayı değeri
         * kadar çağrım olmalıdır, Ne eksik ne Fazla
         */
        verify(listem).add("Ahmet"); // default ta 1 dir
        verify(listem, times(1)).add("Ahmet"); // 1 kere çağrılmış olması beklenir.
        verify(listem, times(3)).add("Leyla"); // 3 kere çağrılmış olması beklenir.
//        verify(listem, times(1)).add("Demet"); // hata verir

        /**
         * Bazen de hiç olmamasınıdı istediğimiz çağrımlar vardır. Bunları kontrol etmek için
         * never() kullanırız.
         */
        verify(listem, never()).add("Ahmet TAS");
//        verify(listem, never()).add("Ahmet "); // Ahmet çağrımı yapılmamalıdıyı kontrol ediyoruz.

        /**
         * en fazla bir kere çağrım yapılmış olmalı
         * [0,1]
         */
        verify(listem, atMostOnce()).add("Ahmet Tas");

        /**
         * en az bir kere çağrım yapılmış olmalı
         * [1,sonsuz]
         */
        verify(listem, atLeastOnce()).add("Leyla");

        /**
         * en az 3 kere çağrım olsun
         * [3,sonsuz]
         */
        verify(listem, atLeast(3)).add("Leyla");

        /**
         * en fazla 6 kere çağrım olsun
         * [0,6]
         */
        verify(listem, atMost(6)).add("Ahmet");


    }

    @Test
    void thenReturn(){
        /**
         * Case, bir sınıf getById ile bizim kullanıcı verilerini iletiyor olsun.
         * -> 1. durum, sorunsuz çalışma, kullanıcı döner.
         * -> 2. durum, kullanıcı id yok ise UserException(User not found)
         * -> 3. durum,invalid metod exception (beklenmeyen bir şey ve bu durumda kodun çalışması beklenmez)
         * public UserProfile getById(Long id){

         }
         */
        Map<Integer,String> okulIsimListesi = mock(HashMap.class);
        when(okulIsimListesi.get(0)).thenReturn("Ali");
        when(okulIsimListesi.get(1)).thenThrow(new ArrayIndexOutOfBoundsException("Boyle bir deger yok"));

        System.out.println("0 index...: " + okulIsimListesi.get(0));
        System.out.println("2 index...: " + okulIsimListesi.get(2));
        System.out.println("1 index...: " + okulIsimListesi.get(1));



    }

    @Test
    void istisnaTest(){
        LinkedList<String> liste = mock(LinkedList.class);
        when(liste.get(100)).thenThrow(new RuntimeException("HATAA")); //
        doThrow(new Exception("BUYUK HATA")).when(liste).clear(); //önce yapalır sonra cagrılır
        liste.clear();
    }

    @Test
    void orderTest(){
        List<String> mockList = mock(ArrayList.class);

        mockList.add("Ahmet");
        mockList.add("Canan");
        mockList.add("Demet");

        verify(mockList).add("Ahmet");
        verify(mockList).add("Canan"); // sırasına bakmadan çağrım yapıldı mı diye bakarım

        InOrder inOrder = inOrder(mockList); // mock listemizi alır bizim vermiş olduğumuz sırayla işlenip işlenmediğine bakar


        inOrder.verify(mockList).add("Ahmet");
        inOrder.verify(mockList).add("Canan"); // ahmet ile canan sırası ile eklenmiş mi
    }

    /**
     *  SPY - Casus nesneler -> gerçek ortamda denemek için kullanılır.
     */
    @Test
    void spyTest(){
        List<String> listem = new ArrayList<>();
        List<String> spyList = spy(listem); // gerçek nesne gibi spy oluşturuldu

        doReturn(100).when(spyList).size(); // 100 dön birisi spyListin size ını çağrırsa

        spyList.add("Muhammet");
        spyList.add("Canan");

        System.out.println("0. index..: " + spyList.get(0)); // gerçek nesne gibi davranacağı için Muhammet
//        System.out.println("10. index..: " + spyList.get(10)); // IndexOutOfBoundsException
        System.out.println("Liste boyutu..: " + spyList.size()); // 100

        verify(spyList).add("Canan"); // gerçek nesne gibi doğrulaması da yapılır


    }


    @Test
    void kimHakli(){
        List<String> liste = new LinkedList<>();
        List<String> spyList = spy(liste);
        spyList.add("Ahmet");
        System.out.println("0. index..: " + spyList.get(0)); // -> Ahmet
        when(spyList.get(anyInt())).thenReturn("Demet");
        System.out.println("0. index..: " + spyList.get(0)); // -> Demet

    }



}
