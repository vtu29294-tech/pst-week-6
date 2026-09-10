class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> list = new ArrayList<>();
        int freqP[] = new int[26];
        int freqS[] = new int[26];

        // add freq. of char 'p' in array
        for(int i = 0; i < p.length(); i++){
            freqP[p.charAt(i) - 'a']++;
        }

        for(int i = 0; i < s.length(); i++){
            freqS[s.charAt(i) - 'a']++;
            //remove the last char of the window if size of window is > p.length
            if(i >= p.length()){
                freqS[s.charAt(i-p.length()) - 'a']--;
            }
            // check both freq. are equal 
            if(Arrays.equals(freqS, freqP)){
                // add index no. in list
                list.add(i-p.length()+1);
            }
        }
        return list;
    }
}