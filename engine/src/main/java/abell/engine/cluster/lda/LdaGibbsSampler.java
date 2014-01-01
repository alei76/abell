package abell.engine.cluster.lda;

public class LdaGibbsSampler {  
 
    /**  
     * 文档词频向量  
     */ 
    int[][] documents;  
 
    /**    
     * 总单词数
     */ 
    int wordCount;  
 
    /**  
     * 话题数  
     */ 
    int topicCount;  
 
    /**  
     * 狄利克雷分布参数
     */ 
    double alpha;  
 
    /**  
     * 狄利克雷分布参数
     */ 
    double beta;  
 
    /**  
     * z[d][w] 第 d 篇文档中第 w 个单词被分配的话题
     */ 
    int z[][];  
 
    /**  
     * nwz[w][z] 单词 w 被分配到话题 k 的次数
     */ 
    int[][] nwz;  
 
    /**  
     * ndz[d][z] 文档 d 中被分配到话题 z 的单词数
     */ 
    int[][] ndz;
 
    /**  
     * nz[z] 话题 z 的单词总数  
     */ 
    int[] nz;  
 
    /**  
     * nwd[d] 文档 d 中的单词总数
     */ 
    int[] nwd;  
 
    /**  
     * cumulative statistics of theta  
     */ 
    double[][] thetasum;  
 
    /**  
     * cumulative statistics of phi  
     */ 
    double[][] phisum;  
 
    /**  
     * size of statistics  
     */ 
    int numstats;  
 
    /**  
     * sampling lag (?)  
     */ 
    private static int THIN_INTERVAL = 20;  
 
    /**  
     * burn-in period  
     */ 
    private static int BURN_IN = 100;  
 
    /**  
     * max iterations  
     */ 
    private static int ITERATIONS = 1000;  
 
    /**  
     * sample lag (if -1 only one sample taken)  
     */ 
    private static int SAMPLE_LAG;  
 
    private static int dispcol = 0;  
 
    /**  
     * Initialise the Gibbs sampler with data.  
     *   
     * @param V  
     *            vocabulary size  
     * @param data  
     */ 
    public LdaGibbsSampler(int[][] documents, int wordCount) {  
 
        this.documents = documents;  
        this.wordCount = wordCount;  
    }  
 
    /**  
     * Initialisation: Must start with an assignment of observations to topics ?  
     * Many alternatives are possible, I chose to perform random assignments  
     * with equal probabilities  
     *   
     * @param topicCount  
     *            number of topics  
     * @return z assignment of topics to words  
     */ 
    public void initialState(int topicCount) {
 
    	nwz = new int[wordCount][topicCount];
        ndz = new int[documents.length][topicCount];
        nz = new int[topicCount];
        nwd = new int[documents.length];
 
        z = new int[documents.length][];
        for (int m = 0; m < documents.length; m++) {  
            int N = documents[m].length;  
            z[m] = new int[N];  
            for (int n = 0; n < N; n++) {  
                int topic = (int) (Math.random() * topicCount);  
                z[m][n] = topic;  
                // number of instances of word i assigned to topic j  
                nwz[documents[m][n]][topic]++;  
                // number of words in document i assigned to topic j.  
                ndz[m][topic]++;  
                // total number of words assigned to topic j.  
                nz[topic]++;  
            }  
            // total number of words in document i  
            nwd[m] = N;  
        }  
    }  
 
    /**  
     * Main method: Select initial state ? Repeat a large number of times: 1.  
     * Select an element 2. Update conditional on other elements. If  
     * appropriate, output summary for each run.  
     *   
     * @param topicCount  
     *            number of topics  
     * @param alpha  
     *            symmetric prior parameter on document--topic associations  
     * @param beta  
     *            symmetric prior parameter on topic--term associations  
     */ 
    public void gibbs(int topicCount, double alpha, double beta) {  
        this.topicCount = topicCount;  
        this.alpha = alpha;  
        this.beta = beta;  
 
        // init sampler statistics  
        if (SAMPLE_LAG > 0) {  
            thetasum = new double[documents.length][topicCount];  
            phisum = new double[topicCount][wordCount];  
            numstats = 0;  
        }  
 
        // initial state of the Markov chain:  
        initialState(topicCount);  
 
        System.out.println("Sampling " + ITERATIONS  
            + " iterations with burn-in of " + BURN_IN + " (B/S=" 
            + THIN_INTERVAL + ").");  
 
        for (int i = 0; i < ITERATIONS; i++) {  
 
            // for all z_i  
            for (int m = 0; m < z.length; m++) {  
                for (int n = 0; n < z[m].length; n++) {  
 
                    // (z_i = z[m][n])  
                    // sample from p(z_i|z_-i, w)  
                    int topic = sampleFullConditional(m, n);  
                    z[m][n] = topic;  
                }  
            }  
 
            if ((i < BURN_IN) && (i % THIN_INTERVAL == 0)) {  
//                System.out.print("B");  
                dispcol++;  
            }  
            // display progress  
            if ((i > BURN_IN) && (i % THIN_INTERVAL == 0)) {  
//                System.out.print("S");  
                dispcol++;  
            }  
            // get statistics after burn-in  
            if ((i > BURN_IN) && (SAMPLE_LAG > 0) && (i % SAMPLE_LAG == 0)) {  
                updateParams();  
//                System.out.print("|");  
                if (i % THIN_INTERVAL != 0)  
                    dispcol++;  
            }  
            if (dispcol >= 100) {  
//                System.out.println();  
                dispcol = 0;  
            }  
        }  
    }
    
    private int sampleFullConditional(int d, int w) {  
 
        // remove z_i from the count variables  
        int topic = z[d][w];  
        nwz[documents[d][w]][topic]--;  
        ndz[d][topic]--;  
        nz[topic]--;  
        nwd[d]--;  
 
        // do multinomial sampling via cumulative method:  
        double[] p = new double[topicCount];  
        for (int k = 0; k < topicCount; k++) {  
            p[k] = (nwz[documents[d][w]][k] + beta) / (nz[k] + wordCount * beta)  
                * (ndz[d][k] + alpha) / (nwd[d] + topicCount * alpha);  
        }  
        // cumulate multinomial parameters  
        for (int k = 1; k < p.length; k++) {  
            p[k] += p[k - 1];  
        }  
        // scaled sample because of unnormalised p[]  
        double u = Math.random() * p[topicCount - 1];  
        for (topic = 0; topic < p.length; topic++) {  
            if (u < p[topic])  
                break;  
        }  
 
        // add newly estimated z_i to count variables  
        nwz[documents[d][w]][topic]++;  
        ndz[d][topic]++;  
        nz[topic]++;  
        nwd[d]++;  
 
        return topic;  
    }  
 
    /**  
     * Add to the statistics the values of theta and phi for the current state.  
     */ 
    private void updateParams() {  
        for (int m = 0; m < documents.length; m++) {  
            for (int k = 0; k < topicCount; k++) {  
                thetasum[m][k] += (ndz[m][k] + alpha) / (nwd[m] + topicCount * alpha);  
            }  
        }  
        for (int k = 0; k < topicCount; k++) {  
            for (int w = 0; w < wordCount; w++) {  
                phisum[k][w] += (nwz[w][k] + beta) / (nz[k] + wordCount * beta);  
            }  
        }  
        numstats++;  
    }  
 
    /**  
     * Retrieve estimated document--topic associations. If sample lag > 0 then  
     * the mean value of all sampled statistics for theta[][] is taken.  
     *   
     * @return theta multinomial mixture of document topics (M x topicCount)  
     */ 
    public double[][] getTheta() {  
        double[][] theta = new double[documents.length][topicCount];  
 
        if (SAMPLE_LAG > 0) {  
            for (int m = 0; m < documents.length; m++) {  
                for (int k = 0; k < topicCount; k++) {  
                    theta[m][k] = thetasum[m][k] / numstats;  
                }  
            }  
 
        } else {  
            for (int m = 0; m < documents.length; m++) {  
                for (int k = 0; k < topicCount; k++) {  
                    theta[m][k] = (ndz[m][k] + alpha) / (nwd[m] + topicCount * alpha);  
                }  
            }  
        }  
 
        return theta;  
    }  
 
    /**  
     * Retrieve estimated topic--word associations. If sample lag > 0 then the  
     * mean value of all sampled statistics for phi[][] is taken.  
     *   
     * @return phi multinomial mixture of topic words (topicCount x V)  
     */ 
    public double[][] getPhi() {  
        System.out.println("topicCount is:"+topicCount+",V is:" + wordCount);  
        double[][] phi = new double[topicCount][wordCount];  
        if (SAMPLE_LAG > 0) {  
            for (int k = 0; k < topicCount; k++) {  
                for (int w = 0; w < wordCount; w++) {  
                    phi[k][w] = phisum[k][w] / numstats;  
                }  
            }  
        } else {  
            for (int k = 0; k < topicCount; k++) {  
                for (int w = 0; w < wordCount; w++) {  
                    phi[k][w] = (nwz[w][k] + beta) / (nz[k] + wordCount * beta);  
                }  
            }  
        }  
        return phi;  
    }  
    /**  
     * Configure the gibbs sampler  
     *   
     * @param iterations  
     *            number of total iterations  
     * @param burnIn  
     *            number of burn-in iterations  
     * @param thinInterval  
     *            update statistics interval  
     * @param sampleLag  
     *            sample interval (-1 for just one sample at the end)  
     */ 
    public void configure(int iterations, int burnIn, int thinInterval,  
        int sampleLag) {  
        ITERATIONS = iterations;  
        BURN_IN = burnIn;  
        THIN_INTERVAL = thinInterval;  
        SAMPLE_LAG = sampleLag;  
    }  
 
    /**  
     * Driver with example data.  
     *   
     * @param args  
     */ 
    public static void main(String[] args) {  
 
        // words in documents  
        int[][] documents = {   
            {1, 4, 3, 2, 3, 1, 4, 3, 2, 3, 1, 4, 3, 2, 3, 6},  
            {2, 2, 4, 2, 4, 2, 2, 2, 2, 4, 2, 2},  
            {1, 6, 5, 6, 0, 1, 6, 5, 6, 0, 1, 6, 5, 6, 0, 0},  
            {5, 6, 6, 2, 3, 3, 6, 5, 6, 2, 2, 6, 5, 6, 6, 6, 0},  
            {2, 2, 4, 4, 4, 4, 1, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 0},  
            {5, 4, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2},  
            
            };  
          
 
        // vocabulary  
        int wordCount = 7;  
        // # topics  
        int topicCount = 2;  
        // good values alpha = 2, beta = .5  
        double alpha = 2;  
        double beta = .5;  
 
        System.out.println("Latent Dirichlet Allocation using Gibbs Sampling.");  
 
        LdaGibbsSampler lda = new LdaGibbsSampler(documents, wordCount);  
        lda.configure(10000, 2000, 100, 10);  
        lda.gibbs(topicCount, alpha, beta);//用gibbs抽样  
 
        double[][] theta = lda.getTheta();//Theta是我们所希望的一种分布可能  
        double[][] phi = lda.getPhi();  
 
        System.out.println();  
        System.out.println();  
        System.out.println("Document--Topic Associations, Theta[d][k] (alpha=" 
            + alpha + ")");  
        System.out.print("d//k/t");  
        for (int m = 0; m < theta[0].length; m++) {  
            System.out.print("   " + m % 10 + "    ");  
        }  
        System.out.println();  
        for (int m = 0; m < theta.length; m++) {  
            System.out.print(m + "/t");  
            for (int k = 0; k < theta[m].length; k++) {  
                 System.out.print(theta[m][k] + " ");  
//                System.out.print(shadeDouble(theta[m][k], 1) + " ");  
            }  
            System.out.println();  
        }  
        System.out.println();  
        System.out.println("Topic--Term Associations, Phi[k][w] (beta=" + beta  
            + ")");  
 
        System.out.print("k//w/t");  
        for (int w = 0; w < phi[0].length; w++) {  
            System.out.print("   " + w % 10 + "    ");  
        }  
        System.out.println();  
        for (int k = 0; k < phi.length; k++) {  
            System.out.print(k + "/t");  
            for (int w = 0; w < phi[k].length; w++) {  
                 System.out.print(phi[k][w] + " ");  
//                System.out.print(shadeDouble(phi[k][w], 1) + " ");  
            }  
            System.out.println();  
        }  
    }  
   
} 