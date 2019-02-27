package utils;

public class HtmlFilterUtils {
    /**
     * html 符号过滤器 防止用户上传脚本代码导致程序出问题
     * @param input
     * @return 过滤后的上传字符串
     */
    public static String HtmlFilter(String input) {
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;")
                .replaceAll(" ", "&nbsp;")
                .replaceAll("\n", "<br>");
    }
}
