package com.login.demo.model

import com.login.demo.enumerations.TypeUsers
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
  import java.util.Date

@Entity
@Table(name="MyUser")
class User() : UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    @SequenceGenerator(name = "MyUser_seq", sequenceName = "MyUser_seq")
    private var id:Long?=null;

     private lateinit var login: String;

     private  lateinit var password: String;

    private lateinit var username: String;

    @Email(message = "The email format is not valid!")
    private lateinit var email: String;
    @Enumerated(EnumType.ORDINAL)
    private var typeUser: TypeUsers = TypeUsers.USER;
    @Temporal(TemporalType.DATE)
    private lateinit var dateCreated:Date;
    @Temporal(TemporalType.DATE)
    private lateinit var dateUpdate:Date;
    private   var codeVerification: Boolean=false;
    private var codeNumber: Long? =null;
    private var changesAccount:Boolean?=null;


    constructor(login: String, password: String, email: String, name:String):this() {
        this.login = login;
        setPassword(password);
        this.email = email;
        setCodeNumber();
        this.dateUpdate=Date();
        this.dateCreated=Date();
        this.username=name;
        this.changesAccount=false;
    }

    fun getId(): Long? {
        return id;
    }

    fun getCodeVerification(): Boolean? {
        return this.codeVerification;
    }
    fun setCodeVerification(newStatus:Boolean) {
        this.codeVerification=newStatus;
    }

    fun getChangesAccount(): Boolean? {
        return this.changesAccount;
    }
    fun setChangesAccount(newStatus:Boolean) {
        this.changesAccount=newStatus;
    }


    fun getTypeUser():String{
        return typeUser.toString();
    }

    fun getLogin():String{
        return login;
    }



    override fun getUsername(): String {
   return this.username;
     }

    public fun setUsername(name:String) {
        this.username=name;
    }



    fun getEmail():String{
        return email;
    }
    fun getDateCreated():Date{
        return dateCreated;
    }
    fun setLogin(login:String){
        this.login = login;
    }
    fun setPassword(password:String){

         this.password=password;

    }
    fun setEmail(email:String){
        this.email=email;
    }
    fun setCodeNumber() {
    var varFloat:Double = Math.random()*10000000;
        this.codeNumber = varFloat.toLong()
    }

    fun getCodeNumber(): Long? {
         return codeNumber;
    }





    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        if(this.typeUser==TypeUsers.ADMIN) return mutableListOf(SimpleGrantedAuthority("ADMIN"));
        if(this.typeUser==TypeUsers.USER) return mutableListOf(SimpleGrantedAuthority("USER"));
        else{
            return mutableListOf(SimpleGrantedAuthority("USER"));
        }

    }

    override fun getPassword(): String {
        return this.password
    }





}