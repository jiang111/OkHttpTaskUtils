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

import java.util.List;

/**
 * Created by jiang on 16/4/18.
 */
public class Model {

    /**
     * status : 200
     * msg : success
     * data : {"list":[{"id":916,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460943333,"function_id":1,"parameter":"{\"noticep\":525}","detail":"{\"thum\":\"\",\"content\":\"\\u0026#34;sdfsdfsdf\\u0026#34;\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":0}"},{"id":915,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460793990,"function_id":1,"parameter":"{\"noticep\":524}","detail":"{\"thum\":\"\",\"content\":\"121212\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初一年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":914,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460793080,"function_id":1,"parameter":"{\"noticep\":523}","detail":"{\"thum\":\"\",\"content\":\"123123131222\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":895,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460705411,"function_id":1,"parameter":"{\"noticep\":521}","detail":"{\"thum\":\"img/zy001/2016/04/15/153008-9803.jpg\",\"content\":\"ggg\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":887,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460599184,"function_id":1,"parameter":"{\"noticep\":519}","detail":"{\"thum\":\"\",\"content\":\"55\",\"exist_voice\":1,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":1,\"comment_num\":0,\"is_read\":1}"}],"last_id":887}
     */

    @Expose
    private int status;

    @Override
    public String toString() {
        return "Model{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    @Expose
    private String msg;
    /**
     * list : [{"id":916,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460943333,"function_id":1,"parameter":"{\"noticep\":525}","detail":"{\"thum\":\"\",\"content\":\"\\u0026#34;sdfsdfsdf\\u0026#34;\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":0}"},{"id":915,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460793990,"function_id":1,"parameter":"{\"noticep\":524}","detail":"{\"thum\":\"\",\"content\":\"121212\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初一年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":914,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460793080,"function_id":1,"parameter":"{\"noticep\":523}","detail":"{\"thum\":\"\",\"content\":\"123123131222\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":895,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460705411,"function_id":1,"parameter":"{\"noticep\":521}","detail":"{\"thum\":\"img/zy001/2016/04/15/153008-9803.jpg\",\"content\":\"ggg\",\"exist_voice\":0,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":0,\"comment_num\":0,\"is_read\":1}"},{"id":887,"teacher_name":"张伟","avatar":"img/k12cloud/2016/04/11/113322-2419.jpg","sex":1,"module_name":"家校公告","create":1460599184,"function_id":1,"parameter":"{\"noticep\":519}","detail":"{\"thum\":\"\",\"content\":\"55\",\"exist_voice\":1,\"exist_vote\":0,\"object\":\"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级\",\"zan_num\":1,\"comment_num\":0,\"is_read\":1}"}]
     * last_id : 887
     */
    @Expose
    private DataEntity data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        @Expose
        private int last_id;
        /**
         * id : 916
         * teacher_name : 张伟
         * avatar : img/k12cloud/2016/04/11/113322-2419.jpg
         * sex : 1
         * module_name : 家校公告
         * create : 1460943333
         * function_id : 1
         * parameter : {"noticep":525}
         * detail : {"thum":"","content":"\u0026#34;sdfsdfsdf\u0026#34;","exist_voice":0,"exist_vote":0,"object":"初三年级(1)班·初三年级(3)班·初三年级(7)班·初二年级(5)班·初一年级(3)班·初一年级(5)班·初一年级·初二年级·初三年级","zan_num":0,"comment_num":0,"is_read":0}
         */
        @Expose
        private List<ListEntity> list;

        public int getLast_id() {
            return last_id;
        }

        public void setLast_id(int last_id) {
            this.last_id = last_id;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "last_id=" + last_id +
                    ", list=" + list +
                    '}';
        }

        public static class ListEntity {
            @Expose
            private int id;
            @Expose
            private String teacher_name;
            @Expose
            private String avatar;
            @Expose
            private int sex;
            @Expose
            private String module_name;
            @Expose
            private int create;
            @Expose
            private int function_id;
            @Expose
            private String parameter;
            @Expose
            private String detail;

            @Override
            public String toString() {
                return "ListEntity{" +
                        "id=" + id +
                        ", teacher_name='" + teacher_name + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", sex=" + sex +
                        ", module_name='" + module_name + '\'' +
                        ", create=" + create +
                        ", function_id=" + function_id +
                        ", parameter='" + parameter + '\'' +
                        ", detail='" + detail + '\'' +
                        '}';
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public int getCreate() {
                return create;
            }

            public void setCreate(int create) {
                this.create = create;
            }

            public int getFunction_id() {
                return function_id;
            }

            public void setFunction_id(int function_id) {
                this.function_id = function_id;
            }

            public String getParameter() {
                return parameter;
            }

            public void setParameter(String parameter) {
                this.parameter = parameter;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }
        }
    }
}
