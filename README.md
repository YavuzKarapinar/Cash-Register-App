## ⚠️ UYARI
- Bu proje staj projesidir.
- Uygulamanın kodları development branch'i üzerinden devam etmektedir.

# Cash Register Uygulaması
Bu proje, Android platformu için geliştirilmiş bir Cash Register (Yazar Kasa) uygulamasıdır. Uygulama, kullanıcıların satış işlemlerini yönetmelerine, ürün eklemelerine, fiş görüntülemelerine, rapor görüntülemelerine ve ödeme işlemlerini gerçekleştirmelerine olanak tanır.

## Özellikler
Ürün Yönetimi: Ürünleri ekleyebilir, düzenleyebilir ve silebilirsiniz. Ürün bilgileri yerel veritabanında saklanır.

Sepet Yönetimi: Kullanıcılar, ürünleri sepetlerine ekleyebilir, miktarlarını ayarlayabilir ve sepetten çıkarabilir.

Rapor Oluşturma: Yapılan satışların raporlamaları yapılır.

Ödeme İşlemleri: Nakit, kredi kartı, diğer vb. ödeme seçenekleriyle ödemeler yapılabilir.

Veritabanı Yönetimi: SQLite kullanılarak tüm ürün ve işlem bilgileri yerel olarak saklanır.

Bottom Navigation Bar: Kullanıcı dostu bir arayüz sunmak için Bottom Navigation Bar ve ExpandableListView ile gezinme menüsü kullanılır.

## Gereksinimler

Android Studio

Android SDK 24 veya üzeri

Java 8 veya üzeri

## Projeyi Çalıştırma
Bu projeyi yerel makinenize klonlayın:

```
git clone https://github.com/yavuzkarapinar/cash-register-app.git
```

## Nasıl Çalıştırılır
Android Studio ile projeyi açın.

Gerekli SDK ve bağımlılıkların indirildiğinden emin olun.

Uygulamayı bir emülatör veya gerçek cihazda çalıştırmak için "Run" butonuna tıklayın. Ürün tabletler için tasarlanmıştır.

## Kullanım
Uygulama açıldığında, ana ekranda ürün listesi görüntülenir. Ayarlar kısmında yeni ürünler ekleyebilir, mevcut ürünleri düzenleyebilir veya silebilirsiniz. Ayrıca ürünün kendine ait grubunu da ayarlayabilirsiniz.

Bir ürün seçildiğinde, sepet ekranına yönlendirilirsiniz. Buradan ürün miktarını ayarlayabilir ve sepete ekleyebilirsiniz.

Sepet ekranında, eklenen ürünler listelenir ve ödeme işlemini başlatabilirsiniz.

Ödeme tamamlandığında, ekranda fiş çıktısı belirir. 

Ayarlar kısmından günlük alabileceğiniz z raporları ve istediğiniz zaman alabileceğiniz x raporlarına erişebilirsiniz.
