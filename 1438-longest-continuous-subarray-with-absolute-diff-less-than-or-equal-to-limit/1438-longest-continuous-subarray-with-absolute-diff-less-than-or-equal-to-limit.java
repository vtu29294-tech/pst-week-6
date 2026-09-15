class Solution {
    public int longestSubarray(int[] nums, int limit) {
        Deque<Integer> maxDq = new ArrayDeque<>();
        Deque<Integer> minDq = new ArrayDeque<>();

        int left = 0;
        int ans = 0;

        for (int right = 0; right < nums.length; right++) {

            // Maintain decreasing deque for maximum
            while (!maxDq.isEmpty() && nums[maxDq.peekLast()] < nums[right]) {
                maxDq.pollLast();
            }
            maxDq.offerLast(right);

            // Maintain increasing deque for minimum
            while (!minDq.isEmpty() && nums[minDq.peekLast()] > nums[right]) {
                minDq.pollLast();
            }
            minDq.offerLast(right);

            // Shrink window if difference exceeds limit
            while ((long) nums[maxDq.peekFirst()] - nums[minDq.peekFirst()] > limit) {
                if (maxDq.peekFirst() == left)
                    maxDq.pollFirst();

                if (minDq.peekFirst() == left)
                    minDq.pollFirst();

                left++;
            }

            ans = Math.max(ans, right - left + 1);
        }

        return ans;
    }
}