本项目所有Model的OR Mapping规则：
1.Model对象和数据表采用驼峰命名规则，Model对象中无@Table注解，hibernate采用默认规则处理;
	例如：“UserGroup”对应数据表“USER_GROUP”
2.对象属性和数据表字段也采用驼峰命名规则，Model对象中属性无@Column(name = "***")注解，hibernate采用默认规则处理;
	例如：Model对象属性“userName”对应数据表字段“USER_NAME”
3.Model对象的CASCADE（级联）统一设置成SAVE_UPDATE;
4.Model对象的FetchType（关联）关系统一设置成LAZY，使用时在程序中显示调用hibernate initialize方法;