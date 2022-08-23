package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} AND username = #{username}")
    Credential getCredential(@Param("url") String url, @Param("username") String username);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredentialById(@Param("credentialid") Integer credentialid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentialsByUserId(@Param("userid") Integer userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, keyName, password, userid) VALUES " +
            "(#{url}, #{username}, #{keyName}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void save(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} WHERE credentialid = #{credentialid} AND userid = #{userid}")
    void update(@Param("credentialid") Integer credentialid,
                @Param("url") String url, @Param("username") String username, @Param("password") String password, @Param("userid") Integer userid);

    @Delete("DELETE FROM CREDENTIALS WHERE credentiald = #{credentialid} AND userid = #{userid}")
    void delete(@Param("credentialid") Integer credentialid, @Param("userid") Integer userid);

}
