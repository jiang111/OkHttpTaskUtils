/**
 * created by jiang, 16/4/18
 * Copyright (c) 2016, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.jiang.android.okhttptaskutils;

import com.google.gson.annotations.Expose;

/**
 * Created by jiang on 16/4/18.
 */
public class Model {


    /**
     * statusCode : 200
     * data : {"userInfo":{"id":"9f484010-ab94-11e5-a010-356b3b2515d4","username":"姜跃松","profession":"teacher","active":true,"phone":"15240393098","grade":"其它","subject":"语文","isLocal":true,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOiI5ZjQ4NDAxMC1hYjk0LTExZTUtYTAxMC0zNTZiM2IyNTE1ZDQiLCJwcm9mZXNzaW9uIjoidGVhY2hlciIsImlhdCI6MTQ3OTM3MjY3Mn0.M7r5t-gggIlM0PqtfA7UOopEe-vLmEyi9ltqy4o2drI","myVideos":2}}
     */

    @Expose
    private int statusCode;
    @Expose
    private DataEntity data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * userInfo : {"id":"9f484010-ab94-11e5-a010-356b3b2515d4","username":"姜跃松","profession":"teacher","active":true,"phone":"15240393098","grade":"其它","subject":"语文","isLocal":true,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOiI5ZjQ4NDAxMC1hYjk0LTExZTUtYTAxMC0zNTZiM2IyNTE1ZDQiLCJwcm9mZXNzaW9uIjoidGVhY2hlciIsImlhdCI6MTQ3OTM3MjY3Mn0.M7r5t-gggIlM0PqtfA7UOopEe-vLmEyi9ltqy4o2drI","myVideos":2}
         */
        @Expose
        private UserInfoEntity userInfo;

        public UserInfoEntity getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoEntity userInfo) {
            this.userInfo = userInfo;
        }

        public static class UserInfoEntity {
            /**
             * id : 9f484010-ab94-11e5-a010-356b3b2515d4
             * username : 姜跃松
             * profession : teacher
             * active : true
             * phone : 15240393098
             * grade : 其它
             * subject : 语文
             * isLocal : true
             * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhY2NvdW50SWQiOiI5ZjQ4NDAxMC1hYjk0LTExZTUtYTAxMC0zNTZiM2IyNTE1ZDQiLCJwcm9mZXNzaW9uIjoidGVhY2hlciIsImlhdCI6MTQ3OTM3MjY3Mn0.M7r5t-gggIlM0PqtfA7UOopEe-vLmEyi9ltqy4o2drI
             * myVideos : 2
             */
            @Expose
            private String id;
            @Expose
            private String username;
            @Expose
            private String profession;
            @Expose
            private boolean active;
            @Expose
            private String phone;
            @Expose
            private String grade;
            @Expose
            private String subject;
            @Expose
            private boolean isLocal;
            @Expose
            private String token;
            @Expose
            private int myVideos;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getProfession() {
                return profession;
            }

            public void setProfession(String profession) {
                this.profession = profession;
            }

            public boolean isActive() {
                return active;
            }

            public void setActive(boolean active) {
                this.active = active;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public boolean isIsLocal() {
                return isLocal;
            }

            public void setIsLocal(boolean isLocal) {
                this.isLocal = isLocal;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public int getMyVideos() {
                return myVideos;
            }

            public void setMyVideos(int myVideos) {
                this.myVideos = myVideos;
            }
        }
    }
}
