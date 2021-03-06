import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ian
 *
 * 一个简单的Json字符串生成器，只需输入属性名数组{@code String[]}及其对应的属性值数组
 * {@code Object[] or Object[][] or List<Object[]>}，就可以输出Json格式的字符串
 *
 */

public class Json {

    private static boolean isObjectArray(Object o){
        if(o==null){
            return false;
        }
        String typeName=o.getClass().getTypeName();
        typeName=typeName.replaceFirst("^java\\.lang.","");
        if(typeName.matches("^[a-z].*")){
            throw new RuntimeException("请输入对象数组");
        }
        return typeName.matches(".*[\\[][]]");
    }

    private static String toValue(Object o){
        if (o==null){
            return "null";
        }
        String typeName=o.getClass().getTypeName();
        if(typeName.matches(".*[\\[][]]")){
            throw  new RuntimeException("请输入对象数组");
        }
        typeName=typeName.replaceFirst("^java\\.lang\\.","");
        if(typeName.equals("String")){
            return "\""+o.toString()+"\"";
        }else{
            return o.toString();
        }
    }

    private static String toValue(Object[]o){
        if (o==null){
            return "null";
        }
        String typeName=o.getClass().getTypeName();
        typeName=typeName.replaceFirst("^java\\.lang\\.","")
                .replaceFirst("[\\[][]]","");
        if(typeName.equals("String")){
            StringBuilder json=new StringBuilder("[");
            for(Object x:o){
                json.append("\"").append(x).append("\", ");
            }
            int len=json.length();
            json.replace(len-2,len,"]");
            return json.toString();
        }else{
            return Arrays.toString(o);
        }
   }

   private static String getClassJson(String[]field,Object[]value){
        int len= field.length;
        if(len!= value.length){
            throw new RuntimeException("属性数组与值数组长度不等");
        }
        StringBuilder json=new StringBuilder("{");
        for(int i=0;i<len;i++){
            if(isObjectArray(value[i])){
                Object[]a=(Object[])value[i];
                json.append("\"").append(field[i]).append("\":")
                        .append(toValue(a)).append(",");
            }else {
                Object a=value[i];
                json.append("\"").append(field[i]).append("\":")
                        .append(toValue(a)).append(",");
            }
        }
       int jsonLen=json.length();
       json.replace(jsonLen-1,jsonLen,"}");
       return json.toString();
   }

    /**
     *
     * @param field 属性值数组{@code String[]}
     * @param value 属性名数组{@code Object[]}
     *
     * <p>
     *    使用举例：
     *            输入:Json.getJson(new String[]{"name","age"},new Object[]{"ian",20});
     *
     *            输出:{"name":"ian","age":20}
     * </p>
     *
     * @return 返回生成的Json对象字符串
     *
     */

   public static String getJson(String[]field,Object[]value){
        return getClassJson(field, value);
   }


    /**
     *
     * @param field 属性值数组{@code String[][]}
     * @param value 属性名数组{@code Object[]}
     *
     *<p>
     *    使用举例：
     *            输入:Json.getJson(new String[]{"name","age"},new Object[][]{{"ian",20},{"Beth",20}});
     *
     *            输出:[{"name":"ian","age":20},{"name":"Beth","age":20}]
     *</p>
     *
     * @return 可以进行多组属性值的拼接，返回生成的Json对象数组字符串
     *
     */

   public static String getJson(String[]field,Object[][]value){
        StringBuilder json=new StringBuilder("[");
        for(Object[]x:value){
            json.append(getClassJson(field,x)).append(",");
        }
        int len=json.length();
        json.replace(len-1,len,"]");
        return json.toString();
    }

    /**
     *
     * @param field 属性值数组{@code String[][]}
     * @param value 属性名数组{@code List<Object[]>}
     *<p>
     *    使用举例：
     *            输入:Json.getJson(new String[]{"name","age"},new ArrayList<Object[]>
     *                 (){{add(new Object[]{"ian","20"});add(new Object[]{"Beth","20"});}});
     *
     *            输出:[{"name":"ian","age":20},{"name":"Beth","age":20}]
     *</p>
     *
     * @return 可以进行多组属性值的拼接，返回生成的Json对象数组字符串
     *
     */

    public static String getJson(String[]field, List<Object[]>value){
        StringBuilder json=new StringBuilder("[");
        for(Object[]x:value){
            json.append(getClassJson(field,x)).append(",");
        }
        int len=json.length();
        json.replace(len-1,len,"]");
        return json.toString();
    }
}
