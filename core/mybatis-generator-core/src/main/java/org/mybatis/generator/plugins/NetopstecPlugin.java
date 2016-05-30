package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by sanze on 2016/5/27.
 */
public class NetopstecPlugin extends PluginAdapter {

    private boolean hasLombok;

    @Override
    public boolean validate(List<String> warnings) {
        //不需要验证，始终返回true
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        hasLombok = Boolean.valueOf(properties.getProperty("hasLombok"));
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (hasLombok) {
            topLevelClass.addImportedType("lombok.Getter");
            topLevelClass.addImportedType("lombok.Setter");

            topLevelClass.addAnnotation("@Getter");
            topLevelClass.addAnnotation("@Setter");
        }
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine("* created by Mybatis Generator " + new Date().toString());
        topLevelClass.addJavaDocLine("*/");
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine("* created by Mybatis Generator " + new Date().toString());
        interfaze.addJavaDocLine("*/");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (hasLombok) {
            return false;
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (hasLombok) {
            return false;
        }
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        List<IntrospectedColumn> introspectedColumns = introspectedTable.getNonPrimaryKeyColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (introspectedColumn.getJdbcType() == Types.TIMESTAMP) {
                introspectedColumn.setJdbcTypeName("DATETIME");
            }
            if (introspectedColumn.getJdbcType() == Types.BIT
                    || introspectedColumn.getJdbcType() == Types.TINYINT) {
                introspectedColumn.setFullyQualifiedJavaType(new FullyQualifiedJavaType(Integer.class.getName()));
            }
        }
    }
}
