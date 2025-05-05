JPA Repostroity nasıl çalışır

Mantık Arkası: Metot İsmi => SQL Query

Spring şu parçaları çözümler:
    find, exists, count, delete gibi anahtarlar
    Alan adları (field names) → Entity içindeki değişken ismi
    Bağlaçlar → And, Or, Between, Like, In, Not

 ÖRN:
 Metot İsmi	                                                              Üretilen Sorgu
 findByUsername(String username)	                                    WHERE username = ?
 findByEmailAndStatus(String email, String status)	                WHERE email = ? AND status = ?
 existsByEmail(String email)	                                  SELECT COUNT(*) > 0 WHERE email = ?
 deleteById(Long id)	                                             DELETE FROM ... WHERE id = ?

 ÖZEL SORGULAR
@Query annotasyonu

 @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
 Optional<UserEntity> getUserByUsername(@Param("username") String username);




