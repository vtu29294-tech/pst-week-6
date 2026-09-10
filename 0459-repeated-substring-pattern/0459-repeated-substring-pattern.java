class Solution {
    public boolean repeatedSubstringPattern(String s) {
        int idx = (s + s).indexOf(s, 1);
        return  idx < s.length();
    }
}